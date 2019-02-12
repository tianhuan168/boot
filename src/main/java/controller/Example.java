package controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import service.UserService;

/**
 * @author: tianhuan
 * @description:
 * @Date: 2018/11/15 10:12
 */


@RestController
@RequestMapping("/test")
public class Example{

    @Autowired
    private UserService userService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private AmqpTemplate amqpTemplate;

    @RequestMapping("/demo")
    public String demo(){
        redisTemplate.opsForHash().put("T","test","2");
        System.out.println(redisTemplate.opsForHash().get("T","test"));
        send();
        return JSONObject.toJSONString(userService.findAll());
    }



    private void send(){
        String message = "hello world!";
        this.amqpTemplate.convertAndSend("hello",message);
    }

}
