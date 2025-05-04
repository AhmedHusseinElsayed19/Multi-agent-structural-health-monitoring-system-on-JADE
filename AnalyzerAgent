import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.core.AID;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AnalyzerAgent extends Agent {
    @Override
    protected void setup() {
        System.out.println("AnalyzerAgent started.");

        addBehaviour(new CyclicBehaviour() {
            @Override
            public void action() {
                ACLMessage msg = receive(); // Receive IMU data from DisplayAgent
                if (msg != null && msg.getPerformative() == ACLMessage.INFORM) {
                    String content = msg.getContent();
                    System.out.println("Received IMU data: " + content);

                    // Parse IMU data (x, y, z)
                    String[] values = content.split(",");
                    float x = Float.parseFloat(values[0].split("=")[1].trim());
                    float y = Float.parseFloat(values[1].split("=")[1].trim());
                    float z = Float.parseFloat(values[2].split("=")[1].trim());

                    // Perform FFT analysis (example implementation)
                    FFTResult fftResult = analyzeIMUData(x, y, z);
                    System.out.println("FFT Analysis: Frequency=" + fftResult.frequency + ", Amplitude=" + fftResult.amplitude);

                    // Save FFT results to CSV
                    saveFFTRawData(content); // Save raw IMU data used for FFT
                    saveFFTResults(fftResult); // Save FFT results
                } else {
                    block(); // Wait for the next message
                }
            }
        });
    }

    private FFTResult analyzeIMUData(float x, float y, float z) {
        // Dummy FFT logic (replace with actual FFT implementation)
        double frequency = Math.sqrt(x * x + y * y + z * z) * 10; // Example frequency calculation
        double amplitude = Math.abs(x + y + z); // Example amplitude calculation

        return new FFTResult(frequency, amplitude);
    }

    private void saveFFTRawData(String rawData) {
        String filePath = "~/imu_raw_data.csv";
        filePath = System.getProperty("user.home") + "/imu_raw_data.csv";

        try {
            // Create the file if it doesn't exist
            if (!new java.io.File(filePath).exists()) {
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write("Timestamp,x,y,z\n"); // Header for raw IMU data
                }
            }

            // Write raw IMU data to the file
            try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                writer.write(timestamp + "," + rawData + "\n");
                System.out.println("Raw IMU data saved to CSV: " + timestamp + "," + rawData);
            }
        } catch (IOException e) {
            System.err.println("Error saving raw IMU data to CSV: " + e.getMessage());
        }
    }

    private void saveFFTResults(FFTResult result) {
        String filePath = "~/fft_data.csv";
        filePath = System.getProperty("user.home") + "/fft_data.csv";

        try {
            // Create the file if it doesn't exist
            if (!new java.io.File(filePath).exists()) {
                try (FileWriter writer = new FileWriter(filePath)) {
                    writer.write("Timestamp,Frequency,Amplitude\n"); // Header for FFT results
                }
            }

            // Write FFT results to the file
            try (FileWriter writer = new FileWriter(filePath, true)) { // Append mode
                LocalDateTime now = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = now.format(formatter);

                writer.write(timestamp + "," + result.frequency + "," + result.amplitude + "\n");
                System.out.println("FFT results saved to CSV: " + timestamp + "," + result.frequency + "," + result.amplitude);
            }
        } catch (IOException e) {
            System.err.println("Error saving FFT results to CSV: " + e.getMessage());
        }
    }

    @Override
    protected void takeDown() {
        System.out.println("AnalyzerAgent terminated.");
    }

    // Helper class to store FFT results
    private static class FFTResult {
        double frequency;
        double amplitude;

        FFTResult(double frequency, double amplitude) {
            this.frequency = frequency;
            this.amplitude = amplitude;
        }
    }
}
