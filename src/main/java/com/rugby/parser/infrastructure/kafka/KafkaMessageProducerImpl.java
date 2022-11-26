package com.rugby.parser.infrastructure.kafka;

import com.google.gson.Gson;
import com.rugby.parser.domain.ports.MessageProducer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

@Service
public class KafkaMessageProducerImpl implements MessageProducer {

    private static final Logger LOGGER = Logger.getLogger(KafkaMessageProducerImpl.class.getName());
    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaMessageProducerImpl(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(List<String> messageList) {
        String message = new Gson().toJson(messageList);

        ListenableFuture<SendResult<String, String>> future = kafkaTemplate.send("channel", message);
        future.addCallback(new ListenableFutureCallback<>() {
            @Override
            public void onSuccess(SendResult<String, String> result) {
                LOGGER.info("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }
            @Override
            public void onFailure(Throwable ex) {
                LOGGER.severe("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
