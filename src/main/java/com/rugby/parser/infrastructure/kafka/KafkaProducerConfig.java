
package com.rugby.parser.infrastructure.kafka;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaProducerConfig {
    private final KafkaProducer<String, String> producer;

    public KafkaProducerConfig() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, getKafkaUrlFromProperties());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        producer = new KafkaProducer<>(props);
    }

    private String getKafkaUrlFromProperties() {
        //Since this is run when an event listener is triggered in Init.java, the application.properties have to be loaded here
        try {
            Properties prop = new Properties();
            prop.load(this.getClass().getResourceAsStream("/application.properties"));
            return prop.getProperty("kafka.url");
        } catch (IOException e) {
            Logger.getLogger(KafkaProducerConfig.class.getName()).severe("Couldn't find application.properties file: " + e.getMessage());
        }

        return "";
    }

    @Bean
    public KafkaProducer<String, String> getKafkaClient() {
        return producer;
    }
}