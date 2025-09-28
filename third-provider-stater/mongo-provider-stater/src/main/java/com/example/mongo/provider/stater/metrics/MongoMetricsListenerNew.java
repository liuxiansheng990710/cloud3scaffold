package com.example.mongo.provider.stater.metrics;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.compress.utils.Sets;
import org.apache.commons.text.StringEscapeUtils;
import org.atm.dc.app.config.log.model.MongoLogger;
import org.atm.dc.app.config.response.utils.ApplicationContextRegister;
import org.atm.dc.app.dao.impl.MongoLogDaoImpl;
import org.atm.dc.app.util.JacksonUtils;
import org.atm.dc.enums.TableNameEnum;
import org.atm.dc.model.log.LogEnum;
import org.atm.dc.utils.TypeUtils;
import org.bson.BsonArray;
import org.bson.BsonBoolean;
import org.bson.BsonDocument;
import org.bson.BsonInt32;
import org.bson.BsonInt64;
import org.slf4j.MDC;

import com.mongodb.event.CommandFailedEvent;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;
import com.mongodb.event.CommandSucceededEvent;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * mongo指标监听器，主要监听org.mongodb.driver.protocol.command产生的日志
 * <p>
 *
 * @author : YP
 * @since : 2024/11/18 10:02
 */

@Slf4j
public class MongoMetricsListenerNew implements CommandListener {

    private final ThreadLocal<MongoLogger> commandThreadLocal = new ThreadLocal<>();

    Set<String> commandNames = Sets.newHashSet("aggregate", "count", "distinct", "mapReduce", "delete", "find", "findAndModify", "getLastError", "getMore", "insert", "resetError", "update");

    Set<Class<?>> filterData = Sets.newHashSet(BsonInt64.class, BsonInt32.class);

    Set<String> skipLog = Sets.newHashSet(TableNameEnum.REQUEST_LOG.getName(), TableNameEnum.MONGO_LOG.getName(), TableNameEnum.OKHTTP3_LOG.getName());

    @Override
    public void commandStarted(CommandStartedEvent event) {
        //sql类型
        String commandName = event.getCommandName();
        AtomicBoolean skip = new AtomicBoolean();
        if (Objects.equals(TypeUtils.castToBoolean(MDC.get("isLog"), false), true) || !commandNames.contains(commandName) || filterData.contains(event.getCommand().get(commandName).getClass())) {
            skip.set(true);
            return;
        }
        //文档名
        String collection = event.getCommand().getString(commandName).getValue();
        //日志入库sql不打印
        if (skipLog.stream().map(collection::contains).toList().contains(true)) {
            skip.set(true);
            return;
        }
        String sql = buildMongoCommand(commandName, collection, event.getCommand());
        MongoLogger mongoLog = new MongoLogger();
        //多个连续空白字符（空格、制表符、换行符等）替换为单个空格
        sql = Pattern.compile("[\\s]+").matcher(sql).replaceAll(" ");
        // 替换日期的 $date 为 ISODate
        sql = Pattern.compile("\\{\"\\$date\":\\s*\"([^\"]+?)\"\\}").matcher(sql).replaceAll("ISODate(\"$1\")");
        // 替换 ObjectId 的 $oid 为 ObjectId
        sql = Pattern.compile("\\{\"\\$oid\":\\s*\"([^\"]+?)\"\\}").matcher(sql).replaceAll("ObjectId(\"$1\")");
        mongoLog.setSql(sql);
        mongoLog.setTime(DateUtil.format(new Date(), DatePattern.NORM_DATETIME_MS_PATTERN));
        mongoLog.setDataBase(event.getDatabaseName());
        mongoLog.setCollection(collection);
        mongoLog.setSqlType(commandName);
        mongoLog.setType(LogEnum.MONGO);
        mongoLog.setSkip(skip.get());
        commandThreadLocal.set(mongoLog);
    }

    @Override
    public void commandSucceeded(@NonNull CommandSucceededEvent event) {
        if (Objects.isNull(commandThreadLocal.get()) || commandThreadLocal.get().isSkip() || Objects.equals(TypeUtils.castToBoolean(MDC.get("isLog"), false), true)) {
            MDC.remove("isLog");
            return;
        }
        MongoLogger mongoLog = commandThreadLocal.get();
        mongoLog.setRunTime(event.getElapsedTime(TimeUnit.MILLISECONDS) + "ms");
        mongoLog.setSuccess(Boolean.TRUE);
        log.info(MongoLogger.LOG_PREFIX + StringEscapeUtils.unescapeJava(JacksonUtils.toJson(mongoLog)));
        MongoLogDaoImpl mongoLogDao = ApplicationContextRegister.getBean(MongoLogDaoImpl.class);
        mongoLogDao.saveLog(mongoLog);
        commandThreadLocal.remove();
    }

    @Override
    public void commandFailed(@NonNull CommandFailedEvent event) {
        if (commandThreadLocal.get().isSkip() || Objects.isNull(commandThreadLocal.get()) || Objects.equals(TypeUtils.castToBoolean(MDC.get("isLog"), false), true)) {
            MDC.remove("isLog");
            return;
        }
        MongoLogger mongoLog = commandThreadLocal.get();
        mongoLog.setRunTime(event.getElapsedTime(TimeUnit.MILLISECONDS) + "ms");
        mongoLog.setSuccess(Boolean.FALSE);
        log.info(MongoLogger.LOG_PREFIX + JacksonUtils.toJson(mongoLog));
        MongoLogDaoImpl mongoLogDao = ApplicationContextRegister.getBean(MongoLogDaoImpl.class);
        mongoLogDao.saveLog(mongoLog);
        commandThreadLocal.remove();
    }

    // 构建通用的 MongoDB Shell 命令
    private String buildMongoCommand(String commandName, String collection, BsonDocument command) {
        return switch (commandName) {
            case "insert" -> {
                BsonArray documents = command.getArray("documents");
                yield documents.stream()
                        .map(document -> "db.getCollection(\"" + collection + "\").insert( "
                                + document.asDocument().toJson() + " );")
                        .collect(Collectors.joining("\n"));
            }
            case "update" -> {
                BsonArray updates = command.getArray("updates");
                yield updates.stream()
                        .map(update -> {
                            BsonDocument updateDoc = update.asDocument();
                            boolean up = false;
                            boolean mu = false;
                            if (updateDoc.containsKey("upsert")) {
                                BsonBoolean upsert = updateDoc.getBoolean("upsert");
                                up = Objects.nonNull(upsert) && upsert.getValue();
                            }
                            if (updateDoc.containsKey("multi")) {
                                BsonBoolean multi = updateDoc.getBoolean("multi");
                                mu = Objects.nonNull(multi) && multi.getValue();
                            }
                            if (updateDoc.containsKey("upsert")) {
                                BsonBoolean upsert = updateDoc.getBoolean("upsert");
                                up = Objects.nonNull(upsert) && upsert.getValue();
                            }
                            return "db.getCollection(\"" + collection + "\").update( "
                                    + updateDoc.getDocument("q").toJson()
                                    + ", " + updateDoc.getDocument("u").toJson()
                                    + "," + up + "," + mu + " );";
                        })
                        .collect(Collectors.joining("\n"));
            }
            case "delete" -> {
                BsonArray deletes = command.getArray("deletes");
                yield deletes.stream()
                        .map(delete -> {
                            BsonDocument deleteDoc = delete.asDocument();
                            return "db.getCollection(\"" + collection + "\").delete( "
                                    + deleteDoc.getDocument("q").toJson()
                                    + " );";
                        })
                        .collect(Collectors.joining("\n"));
            }
            case "find" -> {
                BsonDocument filter = command.getDocument("filter");
                BsonDocument sort = command.containsKey("sort") ? command.getDocument("sort") : new BsonDocument();
                yield "db.getCollection(\"" + collection + "\").find( " + filter.toJson() + " ).sort( " + sort.toJson() + " );";
            }
            case "aggregate" -> {
                BsonArray pipeline = command.getArray("pipeline");
                yield "db.getCollection(\"" + collection + "\").aggregate( " + pipeline.toString() + " );";
            }
            case "count" -> {
                BsonDocument query = command.getDocument("query");
                yield "db.getCollection(\"" + collection + "\").count( " + query.toJson() + " );";
            }
            case "distinct" -> {
                String key = command.getString("key").getValue();
                BsonDocument query = command.getDocument("query");
                yield "db.getCollection(\"" + collection + "\").distinct( \"" + key + "\", " + query.toJson() + " );";
            }
            default -> "db.runCommand({" + commandName + ": " + command.toJson() + "});";
        };
    }
}