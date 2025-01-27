package com.example.commons.web.servlet.enums;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.env.Environment;

import com.example.commons.core.utils.ApplicationContextRegister;

/**
 * <p>
 * 服务枚举类
 * </p>
 *
 * @author : 21
 * @since : 2024/9/27 15:29
 */

public enum Servers {

    UNKNOWN(-1, "unknown", "unknown", "/", "未知服务", false),

    API_GATEWAY(2100, "geteway", "api-gateway", "/", "API网关中心", false),

    AUTH_SERVICE(2101, "auth", "provider-auth", "/auth", "Auth服务", true),

    BASE_SERVICE(2102, "base", "provider-base", "/base", "Base服务", true),

    QUARTZ_SERVICE(2103, "quartz", "provider-quartz", "/quartz", "Quartz服务", true),

    ;

    private final int serverId;
    private final String shortName;
    private final String applicationName;
    private final String path;
    private final String desc;
    /**
     * 是否是cloud服务
     */
    private final boolean service;

    Servers(final int serverId, final String shortName, final String applicationName, final String path, final String desc, boolean service) {
        this.serverId = serverId;
        this.shortName = shortName;
        this.applicationName = applicationName;
        this.path = path;
        this.desc = desc;
        this.service = service;
    }

    public int serverId() {
        return this.serverId;
    }

    public String shortName() {
        return this.shortName;
    }

    public boolean isUnkonwn() {
        return this.equals(UNKNOWN);
    }

    public String applicationName() {
        return this.applicationName;
    }

    public String path() {
        return this.path;
    }

    public String desc() {
        return this.desc;
    }

    public boolean service() {
        return this.service;
    }

    /**
     * 根据URL获取对应的服务
     *
     * @param requestUrl
     * @return
     */
    public static Servers getServersByUrl(final String requestUrl) {
        Servers[] enums = Servers.values();
        for (Servers server : enums) {
            if (server.service && requestUrl.startsWith(server.path)) {
                return server;
            }
        }
        return Servers.UNKNOWN;
    }

    /**
     * 获取Server数据
     *
     * @param serverName
     * @return
     */
    public static Servers getApplicationServer(final String serverName) {
        Servers[] enums = Servers.values();
        for (Servers server : enums) {
            if (Objects.equals(serverName, server.shortName()) || Objects.equals(serverName, server.applicationName)) {
                return server;
            }
        }
        return Servers.UNKNOWN;
    }

    /**
     * 根据applicationName获取Server
     *
     * @return
     */
    public static Servers getEnvServerName() {
        AbstractApplicationContext applicationContext = (AbstractApplicationContext) ApplicationContextRegister.getApplicationContext();
        Environment environment = applicationContext.getEnvironment();
        String applicationName = Objects.requireNonNull(environment.getProperty("spring.application.name"));
        String name = System.getProperty("server.name");
        String serverName = StringUtils.isBlank(name) ? applicationName : name;
        return getApplicationServer(serverName);
    }
}
