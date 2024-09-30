package com.example.mongo.provider.stater.metrics;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.text.StringEscapeUtils;
import org.bson.BsonArray;
import org.bson.BsonDocument;

import com.example.commons.core.enums.LogEnum;
import com.example.commons.core.model.log.MongoLog;
import com.example.commons.core.utils.JacksonUtils;
import com.google.common.collect.Sets;
import com.mongodb.event.CommandListener;
import com.mongodb.event.CommandStartedEvent;

import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * mongo指标监听器，主要监听org.mongodb.driver.protocol.command产生的日志
 * </p>
 *
 * @author : 21
 * @since : 2024/9/29 11:27
 */

@Slf4j
public class MongoMetricsListener implements CommandListener {

    private final ThreadLocal<MongoLog> commandThreadLocal = new ThreadLocal<>();

    Set<String> commandNames = Sets.newHashSet(
            "aggregate",
            "count",
            "distinct",
            "mapReduce",
            "delete",
            "find",
            "findAndModify",
            "getLastError",
            "getMore",
            "insert",
            "resetError",
            "update");

    @Override
    public void commandStarted(CommandStartedEvent event) {
        //sql类型
        String commandName = event.getCommandName();
        if (!commandNames.contains(commandName)) {
            return;
        }
        //文档名
        String collection = event.getCommand().getString(commandName).getValue();
        String sql = buildMongoCommand(commandName, collection, event.getCommand());
        MongoLog mongoLog = new MongoLog();
        //多个连续空白字符（空格、制表符、换行符等）替换为单个空格
        sql = Pattern.compile("[\\s]+").matcher(sql).replaceAll(" ");
        // 替换日期的 $date 为 ISODate
        sql = Pattern.compile("\\{\"\\$date\":\\s*\"([^\"]+?)\"\\}").matcher(sql).replaceAll("ISODate(\"$1\")");
        // 替换 ObjectId 的 $oid 为 ObjectId
        sql = Pattern.compile("\\{\"\\$oid\":\\s*\"([^\"]+?)\"\\}").matcher(sql).replaceAll("ObjectId(\"$1\")");
        mongoLog.setSql(sql);
        mongoLog.setDataBase(event.getDatabaseName());
        mongoLog.setCollection(collection);
        mongoLog.setSqlType(commandName);
        mongoLog.setType(LogEnum.MONGO);
        commandThreadLocal.set(mongoLog);
    }

    @Override
    public void commandSucceeded(com.mongodb.event.CommandSucceededEvent event) {
        if (!commandNames.contains(event.getCommandName())) {
            return;
        }
        MongoLog mongoLog = commandThreadLocal.get();
        mongoLog.setRunTime(event.getElapsedTime(TimeUnit.MILLISECONDS) + "ms");
        mongoLog.setSuccess(Boolean.TRUE);
        log.info(MongoLog.LOG_PREFIX + StringEscapeUtils.unescapeJava(JacksonUtils.toJson(mongoLog)));
        commandThreadLocal.remove();
    }

    @Override
    public void commandFailed(com.mongodb.event.CommandFailedEvent event) {
        if (!commandNames.contains(event.getCommandName())) {
            return;
        }
        MongoLog mongoLog = commandThreadLocal.get();
        mongoLog.setRunTime(event.getElapsedTime(TimeUnit.MILLISECONDS) + "ms");
        mongoLog.setSuccess(Boolean.FALSE);
        log.info(MongoLog.LOG_PREFIX + JacksonUtils.toJson(mongoLog));
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
                            return "db.getCollection(\"" + collection + "\").update( "
                                    + updateDoc.getDocument("q").toJson()
                                    + ", " + updateDoc.getDocument("u").toJson()
                                    + ", {upsert: " + updateDoc.getBoolean("upsert").getValue()
                                    + "} );";
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