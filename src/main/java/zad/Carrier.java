package zad;

import zad.middleware.*;

import java.util.*;

public class Carrier {
    private String id;
    private Set<String> services = new HashSet<>();
    private SpaceWriter adminsWriter = new SpaceWriter();
    private SpaceWriter agencyWriter = new SpaceWriter();
    private String adminExchange = "admins_space";

    public static void main(String[] args) throws Exception {
        Carrier carrier = new Carrier();

        carrier.start();
    }

    private void start() throws Exception {
        services.add("people_delivery");
        services.add("load_delivery");
        services.add("satellite_placement");
        System.out.println("Enter Carrier id");
        Scanner scanner = new Scanner(System.in);
        id = scanner.nextLine();
        System.out.println("Pick two services (people_delivery, load_delivery, satellite_placement)");

        List<String> pickedServices = new ArrayList<>();
        while (pickedServices.size() < 2) {
            String service = scanner.nextLine();
            if (!services.contains(service)) {
                System.out.println("bad name of service");
                continue;
            }
            if (pickedServices.contains(service)) {
                System.out.println("dont repeat names");
            } else {
                pickedServices.add(service);
            }

        }
        agencyWriter.init("agencyDirect", "direct");
        adminsWriter.init(adminExchange, "topic");
        Handler<String> msgHandler = (msg) -> {
            System.out.println("Received: "+msg);

            String[] splittedMessage = msg.split(":");

            if (splittedMessage.length != 3) {
                System.out.println("bad number of parameters");

            }
            String response = "Acknowledge: {agency: " + splittedMessage[0]+" message_id: "+splittedMessage[1]+ " carrier: "+id +" }";



            try {
                agencyWriter.send(response, "agency." + splittedMessage[0]);
                adminsWriter.send(response, "admins");
            } catch (Exception e) {
                e.printStackTrace();
            }


        };

        new Thread(new ServiceReader("carrier." + pickedServices.get(0), msgHandler).init()).start();
        new Thread(new ServiceReader("carrier." + pickedServices.get(1), msgHandler).init()).start();

        List<String> keys = new ArrayList<>();
        keys.add("admin.carriers");
        keys.add("admin.all");
        new Thread(new SpaceReader(keys, System.out::println).init(adminExchange,"topic")).start();




    }
}
