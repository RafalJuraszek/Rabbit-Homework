package zad.middleware;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ServiceWriter {
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private String key;

    public ServiceWriter(String queueName) {
        key = queueName;

    }


    public void send(String message) throws Exception {
        channel.basicPublish("", key, null, message.getBytes());

    }
    public void init() throws Exception{

        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(key, false, false, false, null);
    }

    public String getKey() {
        return key;
    }
}
