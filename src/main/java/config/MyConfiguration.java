package config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/19 11:53
 */

@Configuration
@Import(value= {DataSourceAutoConfiguration.class,RedisConfig.class,RabbitConfiguration.class,WebConfig.class})
@EnableConfigurationProperties(value = {MyProperties.class,RabbitProperties.class})
@ComponentScan({"controller","service","dao"})
@MapperScan("mapper")
public class MyConfiguration {
}
