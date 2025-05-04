# 🏗️ Multi-Agent-Based Structural Health Monitoring System for Civil Infrastructure

![Platform](https://img.shields.io/badge/platform-ESP32%20%7C%20RaspberryPi-green)
![Language](https://img.shields.io/badge/language-C++%20%7C%20Java%20%7C%20Python-blue)
![License](https://img.shields.io/badge/license-MIT-lightgrey)

## 🎯 Objective  
Develop a **Multi-Agent System (MAS)** for **Structural Health Monitoring (SHM)** of civil infrastructure, addressing:

- ✅ Real-time data collection  
- ✅ Fault detection  
- ✅ System scalability

---

## 🔧 Key Aspects

### 📦 Sensor Node Configuration  
- **IMU (Motion Sensing):** [Adafruit BNO085](https://www.adafruit.com/product/4754)  
- **Environmental Sensor:** [Adafruit BME280](https://www.adafruit.com/product/2652) – monitors humidity, pressure, and temperature  
- **Microcontroller:** [ESP32-S3-DevKitC-1](https://www.espressif.com/en/products/devkits/esp32-s3-devkitc-1/overview) for edge computing and MQTT communication

### 🤖 Multi-Agent System (MAS) Implementation  
- **Agent Platform:** [JADE Framework](https://jade.tilab.com/) on **Raspberry Pi**  
- **IoT Communication:** [MQTT](https://mqtt.org/) for data transfer between sensor nodes and agent network  
- **Agent Logic:** Each agent processes, reacts to, and coordinates sensor data based on the system context

### 🧠 Methodology  
- Utilized the **AGEME** methodology for modeling and designing agent behaviors and interactions for SHM

---

## 📊 Outcome  
This project:
- Demonstrates a **scalable**, **adaptable**, and **efficient** approach to SHM using MAS
- Enables **real-time monitoring**, **fault diagnosis**, and system decision-making
- Offers a foundation for **deploying MAS in real-world infrastructure**

---

## 🚀 Setup & Installation

### ESP32 Sensor Node  
1. Install **Arduino IDE** with ESP32 board support  
2. Install libraries:  
   - `Adafruit BME280`
   - `Adafruit BNO08x`
   - `PubSubClient` (for MQTT)  
3. Flash the code in `/src/esp32/` to your ESP32-S3 board

### JADE Agent Environment (Raspberry Pi)  
1. Install Java and JADE on the Raspberry Pi  
2. Run agents from `/src/jade_agents/`  
3. Connect to the MQTT broker (e.g., Mosquitto) for agent-node communication

---

## 📌 Requirements

- ESP32-S3 DevKitC-1  
- Raspberry Pi 4 or later  
- Sensors: BNO085 IMU, BME280  
- MQTT Broker (e.g., Mosquitto)  
- Java Runtime Environment for JADE  
- Arduino IDE for ESP32 programming

---

## 📄 License  
This project is licensed under the [MIT License](./MIT_LICENSE).

---

## 🙋‍♂️ Author  
**Ahmed Mahmoud**  
Master’s Thesis – TUHH  
Contact: https://www.linkedin.com/in/ahmed-hussein-mahmoud-b05450111/


---


## 🎥 System Demo

Below is a demonstration of the working multi-agent-based SHM system:

![MAS Demo](mas-demo.gif)

