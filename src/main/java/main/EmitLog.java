package main;/**
 * @author: tianhuan
 * @description:
 * @Date: 2019/1/16 18:44
 */


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 *
 * @author
 * @create 2019-01-16 18:44
 **/
public class EmitLog {
    private static final String EXCHANGE_NAME = "logs";
    private final static String HOSTNAME = "39.105.0.233";
    private final static int PORT = 5672;
    private final static String USERNAME = "admin";
    private final static String PASSWORD = "admin@123";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOSTNAME);
        factory.setUsername(USERNAME);
        factory.setPassword(PASSWORD);
        factory.setPort(PORT);

        final Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        for (int i=0;i<10;i++){
            String message =   "info: Hello World!" + Integer.toString(i) ;
            channel.basicPublish(EXCHANGE_NAME, "", null, message.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + message + "'");
        }


    }
}
