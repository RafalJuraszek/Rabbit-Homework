package zad;

import zad.middleware.SpaceReader;
import zad.middleware.SpaceWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Admin {
    private SpaceWriter spaceWriter;
    private String exchangeName = "admins_space";
    public static void main(String[] args) throws Exception {
        Admin admin = new Admin();
        admin.start();
    }

    private void start() throws Exception {
        init();


        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        int i=1;
        while (true) {
            System.out.println("Pick type of message to send (agencies, carriers, all");
            String service = br.readLine();
            String message = br.readLine();



            if(service.equals("agencies"))
            {
                spaceWriter.send(message,"admin.agencies");
            }
            else if(service.equals("carriers")) {

                spaceWriter.send(message, "admin.carriers");
            }
            else if(service.equals("all"))
            {
                spaceWriter.send(message,"admin.all");
            }
            else{
                System.out.println("bad service type");
            }

        }

    }
    public void init() throws Exception {
        new Thread(new SpaceReader("admins", System.out::println).init(exchangeName, "topic")).start();
        spaceWriter = new SpaceWriter().init(exchangeName, "topic");


    }
}
