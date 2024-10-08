package com.example.commons.web.runner;

import java.util.Properties;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.example.commons.web.utils.ApplicationUtils;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SpringBootApplicationRunner implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) {

        String name = ApplicationUtils.getWebServers().shortName();
        String serverEnvironment = ApplicationUtils.getEnvironment().getEnvironmentStr();
        String port = ApplicationUtils.getPort();

        Properties props = System.getProperties();
        log.info("Operating System Name: {}", props.getProperty("os.name"));
        log.info("Operating System Version: {}", props.getProperty("os.version"));
        log.info("Java Run The Environment Version: {}", props.getProperty("java.version"));
        log.info("{} Start Complete In {} With Port {}", name, serverEnvironment, port);
        log.info("Let's get started!");

    }
}
