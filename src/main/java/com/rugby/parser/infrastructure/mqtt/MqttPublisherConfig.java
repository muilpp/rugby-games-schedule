package com.rugby.parser.infrastructure.mqtt;

import com.rugby.parser.infrastructure.kafka.KafkaProducerConfig;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import java.util.logging.Logger;

@Configuration
public class MqttPublisherConfig {
    private static final Logger LOGGER = Logger.getLogger(MqttPublisherConfig.class.getName());
    private IMqttClient mqttClient;
    private MqttConnectOptions options;

    public MqttPublisherConfig() {
        String publisherId = UUID.randomUUID().toString();

        try {
            mqttClient = new MqttClient(getMqttUrlFromProperties(), publisherId);
            options = new MqttConnectOptions();
            options.setAutomaticReconnect(true);
            options.setCleanSession(true);
            options.setConnectionTimeout(10);
        } catch (MqttException e) {
            LOGGER.severe("Couldn't initialize mqtt client: " + e.getMessage());
        }
    }

    private String getMqttUrlFromProperties() {
        //Since this is run when an event listener is triggered in Init.java, the application.properties have to be loaded here
        try {
            Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("/application.properties"));
            return prop.getProperty("mqtt.url");
        } catch (IOException e) {
            Logger.getLogger(KafkaProducerConfig.class.getName()).severe("Couldn't find application.properties file: " + e.getMessage());
        }

        return "";
    }

    @Bean
    public IMqttClient getMqttClient() {
        try {
            mqttClient.connect(options);
        } catch (MqttException e) {
            LOGGER.severe("Couldn't connect mqtt client: " + e.getMessage());
        }
        return mqttClient;
    }
}