package zad.middleware;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ServiceReader implements Runnable {
    private ConnectionFactory factory = new ConnectionFactory();
    private Connection connection;
    private Channel channel;
    private String key;
    private Handler<String> handler;


    public ServiceReader(String queueName, Handler<String> handler) {
        key = queueName;
        this.handler = handler;

    }

    public ServiceReader init() throws Exception{

        factory.setHost("localhost");
        connection = factory.newConnection();
        channel = connection.createChannel();
        channel.queueDeclare(key, false, false, false, null);
        channel.basicQos(1);
        return this;
    }

    public String getKey() {
        return key;
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
            channel.basicConsume(key, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
