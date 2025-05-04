#include <WiFi.h>
#include <PubSubClient.h>
#include <Wire.h>
#include <Adafruit_BNO08x.h>
#include <BME280SpiSw.h>

// WiFi and MQTT settings
const char* ssid = "xxxxx"; // WiFi SSID
const char* password = "xxxxxxx";  // WiFi password
const char* mqttServer = "xxxxxxx";   // Raspberry Pi's IP address
const int mqttPort = 1883;

// Initialize WiFi and MQTT client
WiFiClient espClient;
PubSubClient client(espClient);

// BME280 settings (SPI mode)
#define CHIP_SELECT_PIN 10
#define MOSI_PIN 11
#define MISO_PIN 12
#define SCK_PIN 13
BME280SpiSw::Settings settings(CHIP_SELECT_PIN, MOSI_PIN, MISO_PIN, SCK_PIN);
BME280SpiSw bme(settings);

// BNO08x initialization
Adafruit_BNO08x bno;

// Timer variables for non-blocking execution
unsigned long lastTime = 0;
const int sendInterval = 20; // Set to 20ms for 50Hz, 10ms for 100Hz

void setup() {
  Serial.begin(115200);
  Wire.begin();

  // Connect to WiFi
  connectToWiFi();

  // Initialize MQTT client
  client.setServer(mqttServer, mqttPort);

  // Initialize BME280
  while (!bme.begin()) {
    Serial.println("Could not find BME280 sensor!");
    delay(1000);
  }
  Serial.println("BME280 initialized successfully");

  // Initialize BNO08x
  if (!bno.begin_I2C()) {
    Serial.println("Failed to find BNO08x sensor!");
    while (1) delay(10);
  }
  Serial.println("BNO08x initialized successfully");

  // Enable BNO08x accelerometer sensor
  if (!bno.enableReport(SH2_ACCELEROMETER)) {
    Serial.println("Failed to enable BNO08x accelerometer");
  } else {
    Serial.println("BNO08x accelerometer enabled");
  }
}

void loop() {
  if (!client.connected()) {
    reconnect();
  }
  client.loop();  // Keep MQTT connection alive

  unsigned long currentTime = millis();
  
  if (currentTime - lastTime >= sendInterval) {
    lastTime = currentTime;

    // Read and publish BME280 data
    String bmeData = readBME280();
    client.publish("sensor/bme280", bmeData.c_str());

    // Read and publish BNO08x data
    String bnoData = readBNO08x();
    client.publish("sensor/bno08x", bnoData.c_str());
  }
}

void connectToWiFi() {
  WiFi.begin(ssid, password);
  while (WiFi.status() != WL_CONNECTED) {
    delay(500);
    Serial.print(".");
  }
  Serial.println("\nConnected to WiFi");
}

void reconnect() {
  while (!client.connected()) {
    Serial.println("Attempting MQTT connection...");
    if (client.connect("ESP32_Sensor")) {
      Serial.println("Connected to MQTT broker");
    } else {
      delay(2000); // Retry after 2 seconds
    }
  }
}

String readBME280() {
  float temp, hum, pres;
  bme.read(pres, temp, hum, BME280::TempUnit_Celsius, BME280::PresUnit_Pa);
  return String("Temp:") + temp + ",Humidity:" + hum + ",Pressure:" + pres;
}

String readBNO08x() {
  sh2_SensorValue_t sensorValue;
  if (bno.getSensorEvent(&sensorValue)) {
    if (sensorValue.sensorId == SH2_ACCELEROMETER) {
      return String("Accel:x=") + sensorValue.un.accelerometer.x +
             ",y=" + sensorValue.un.accelerometer.y +
             ",z=" + sensorValue.un.accelerometer.z;
    }
  }
  return "No data";
}

  

