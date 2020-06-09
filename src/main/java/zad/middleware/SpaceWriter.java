package zad.middleware;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SpaceWriter {

    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;

    String exchangeName = "agencyTopic";

    public SpaceWriter init(String exchangeName, String type) throws Exception{

        this.exchangeName = exchangeName;
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();

        // exchange
        if(type.equals("topic")) {
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.TOPIC);
        } else {
            channel.exchangeDeclare(this.exchangeName, BuiltinExchangeType.DIRECT);
        }
        return this;
    }
    public void send(String message, String key) throws Exception{

            channel.basicPublish(exchangeName, key, null, message.getBytes("UTF-8"));


    }



}
