package zad;


import zad.middleware.SpaceReader;
import zad.middleware.ServiceWriter;
import zad.middleware.SpaceWriter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class Agency {


    private String id;
    private Map<String, ServiceWriter> queues = new HashMap<>();
    private SpaceWriter adminWriter;
    private String adminExchange = "admins_space";

    public Agency() {

    }

    public static void main(String[] args) throws Exception {

        Agency agency = new Agency();

        agency.start();


    }

    private void start() throws Exception {
        // info
        System.out.println("Enter Agency id");
        Scanner scanner = new Scanner(System.in);
        id = scanner.nextLine();
        new Thread(new SpaceReader("agency." + id, System.out::println).init("agencyDirect","direct")).start();
        List<String> keys = new ArrayList<>();
        keys.add("admin.agencies");
        keys.add("admin.all");
        new Thread(new SpaceReader(keys, System.out::println).init(adminExchange,"topic")).start();


        addQueueReferences();
        BufferedReader br = new BufferedReader(new
                InputStreamReader(System.in));
        int i = 1;
        while (true) {
            System.out.println("Enter type of service (people_delivery, load_delivery, satellite_placement");
            String service = scanner.nextLine();
            if (!queues.containsKey(service)) {
                System.out.println("bad service name");
                continue;
            }
            String message = br.readLine();

            String service_id = String.valueOf(i);
            i++;
            message = id + ":" + service_id + ":" + message;
            queues.get(service).send(message);
            adminWriter.send(message, "admins");


        }

    }

    private void addQueueReferences() throws Exception {
        List<String> services = new ArrayList<>();
        services.add("people_delivery");
        services.add("load_delivery");
        services.add("satellite_placement");

        for (String service : services) {
            ServiceWriter serviceWriter = new ServiceWriter("carrier." + service);
            serviceWriter.init();
            queues.put(service, serviceWriter);
        }
        adminWriter = new SpaceWriter().init(adminExchange, "topic");

    }

}
