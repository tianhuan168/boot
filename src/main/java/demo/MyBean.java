package demo;



import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/15 18:41
 */

@Component
public class MyBean implements CommandLineRunner{


    private Duration sessionTimeout = Duration.ofSeconds(30);

    @Override
    public void run(String... strings) throws Exception {
        System.out.println(strings);
    }
}
