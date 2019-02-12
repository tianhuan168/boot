package main;

import config.MyConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Import;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/15 10:13
 */
@SpringBootApplication
@Import(MyConfiguration.class)
@EnableEurekaClient
public class Main {
    public static void main(String[] args) throws Exception {
        SpringApplication application = new SpringApplication(Main.class);
        application.run(args);

    }

}
