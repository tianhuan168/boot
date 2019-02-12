package config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/19 11:49
 */
@ConfigurationProperties(prefix = "my.test")
public class MyProperties {

    private String name;
    private int age;
    private Boolean man;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Boolean getMan() {
        return man;
    }

    public void setMan(Boolean man) {
        this.man = man;
    }
}
