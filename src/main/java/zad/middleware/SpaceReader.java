package zad.middleware;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpaceReader implements Runnable {
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private List<String> keys = new ArrayList<>();
    private Handler<String> handler;
    private String exchangeName= "agencyTopic";
    private String queueName;


    public SpaceReader(List<String> keys, Handler<String> handler) {
        this.keys = keys;
        this.handler = handler;

    }
    public SpaceReader(String key, Handler<String> handler) {
        keys.add(key);
        this.handler = handler;

    }

    public SpaceReader init(String exchangeName, String type) throws Exception{
        this.exchangeName = exchangeName;
        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        if(type.equals("topic")) {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        }
        else {
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.DIRECT);
        }

        queueName = channel.queueDeclare().getQueue();
        for(String key : keys) {
            channel.queueBind(queueName, exchangeName, key);
        }
        return this;
    }



    @Override
    public void run() {


        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                handler.handle(message);

            }
        };

        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
