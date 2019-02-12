package demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author
 * @create 2018-11-15 18:44
 **/
@Component
@ConfigurationProperties(prefix = "my")
public class Mybean1 {

    @Value("${name}")
    private String name;

}
