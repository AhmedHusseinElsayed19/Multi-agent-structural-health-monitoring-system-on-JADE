import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;

public class DisplayAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("DisplayAgent started.");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive(); // Receive messages from ManagerAgent

                if (msg != null) {
                    if (msg.getPerformative() == ACLMessage.INFORM) {
                        String content = msg.getContent();

                        // Ignore acknowledgment messages
                        if (!content.equals("Ready for next iteration.")) {
                            System.out.println("Received sensor data: " + content);

                            // Request the next iteration
                            ACLMessage request = new ACLMessage(ACLMessage.REQUEST);
                            request.addReceiver(new AID("manager", AID.ISLOCALNAME));
                            request.setContent("Next iteration please.");
                            send(request);

                            System.out.println("Requested next iteration from ManagerAgent.");
                        }
                    } else {
                        System.out.println("Unsupported message type: " + msg.getPerformative());
                    }
                } else {
                    block(); // Wait for the next message
                }
            }
        });
    }

    @Override
    protected void takeDown() {
        System.out.println("DisplayAgent terminated.");
    }
}
