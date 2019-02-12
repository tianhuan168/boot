package main;/**
 * @author: tianhuan
 * @description:
 * @Date: 2019/1/1 10:41
 */


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author
 * @create 2019-01-01 10:41
 **/
public class Send {
    private final static String QUEUE_NAME = "2019-01-01";
    private final static String HOSTNAME = "39.105.0.233";
    private final static int PORT = 5672;
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin@123";

    public static void main(String[] argv) throws Exception {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setPort(PORT);

        try (Connection connection = factory.newConnection();
                Channel channel = connection.createChannel()) {
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            String message = "Hello World!~~~";
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }
    }
}
