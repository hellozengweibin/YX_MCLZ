package com.eshore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author eshore
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
// @MapperScan(basePackages = {"com.eshore.*"})
public class EshoreApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(EshoreApplication.class, args);
    }
}
