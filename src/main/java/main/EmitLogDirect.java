package main;/**
 * @author: tianhuan
 * @description:
 * @Date: 2019/1/16 19:05
 */


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 *
 * @author
 * @create 2019-01-16 19:05
 **/
public class EmitLogDirect {
    private static final String EXCHANGE_NAME = "direct_logs";
    private final static String HOSTNAME = "39.105.0.233";
    private final static int PORT = 5672;
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin@123";
    public static void main(String[] argv) throws  Exception{
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setPort(PORT);

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        List<String> list = Arrays.asList("INFO","ERROR","WARNING");
        Random ran1 = new Random();
        for (int i=0;i<10;i++){
            String severity = list.get(ran1.nextInt(3));
            String message = severity + "~~~" +  Integer.toString(i+1);
            channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + severity + "':'" + message + "'");
        }
    }
}
