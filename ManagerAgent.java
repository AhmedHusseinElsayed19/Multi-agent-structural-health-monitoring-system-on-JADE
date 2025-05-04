import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import org.eclipse.paho.client.mqttv3.*;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ManagerAgent extends Agent {
    private MqttClient mqttClient;

    @Override
    protected void setup() {
        System.out.println("ManagerAgent started.");

        try {
            String broker = "tcp://localhost:1883";
            String clientId = "ManagerAgent";
            mqttClient = new MqttClient(broker, clientId);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {}

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String payload = new String(message.getPayload());
                    System.out.println("Received data from topic [" + topic + "]: " + payload);

                    // Save data to CSV
                    saveToCSV(topic, payload);

                    // Send sensor data to DisplayAgent using FIPA INFORM
                    ACLMessage informMsg = new ACLMessage(ACLMessage.INFORM);
                    informMsg.addReceiver(new AID("display", AID.ISLOCALNAME));
                    informMsg.setContent(payload); // Set the actual sensor data as content
                    send(informMsg);

                    System.out.println("Sent sensor data to DisplayAgent: " + payload);
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {}
            });

            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            mqttClient.connect();
            mqttClient.subscribe("sensor/bme280");
            mqttClient.subscribe("sensor/bno08x");

            System.out.println("ManagerAgent is now subscribed to sensor topics.");

            // Listen for requests from DisplayAgent
            addBehaviour(new CyclicBehaviour() {
                @Override
                public void action() {
                    ACLMessage request = receive();
                    if (request != null && request.getPerformative() == ACLMessage.REQUEST) {
                        System.out.println("Received REQUEST from DisplayAgent for next iteration.");
                        ACLMessage reply = request.createReply();
                        reply.setPerformative(ACLMessage.INFORM);
                        reply.setContent("Ready for next iteration.");
                        send(reply);
                    } else {
                        block();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveToCSV(String topic, String data) {
        String filePath = "~/sensor_data.csv";
        filePath = System.getProperty("user.home") + "/sensor_data.csv";

        try {
            if (!new java.io.File(filePath).exists()) {
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write("Timestamp,Sensor,Temp,Humidity,Pressure,x,y,z\n");
                }
            }

            try (FileWriter writer = new FileWriter(filePath, true)) {
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                StringBuilder csvRow = new StringBuilder(timestamp).append(",").append(topic);

                if (topic.equals("sensor/bme280")) {
                    String[] values = data.split(",");
                    for (String value : values) {
                        String[] parts = value.split(":");
                        if (parts.length == 2) csvRow.append(",").append(parts[1].trim());
                    }
                    csvRow.append(",,"); // Placeholders for BNO08x
                } else if (topic.equals("sensor/bno08x")) {
                    csvRow.append(",,,"); // Placeholders for BME280
                    String[] values = data.split(",");
                    for (String value : values) {
                        String[] parts = value.split("=");
                        if (parts.length == 2) csvRow.append(",").append(parts[1].trim());
                    }
                }

                writer.write(csvRow.toString() + "\n");
                System.out.println("Data saved to CSV: " + csvRow.toString());
            }
        } catch (IOException e) {
            System.err.println("Error saving data to CSV: " + e.getMessage());
        }
    }

    @Override
    protected void takeDown() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) mqttClient.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("ManagerAgent terminated.");
    }
}
