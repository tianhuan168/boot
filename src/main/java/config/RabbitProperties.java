package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/28 14:18
 */
@ConfigurationProperties(prefix = "spring.rabbitmq")
public class RabbitProperties {

    private String host;
    private int port = 5672;
    private String username;
    private String password;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
