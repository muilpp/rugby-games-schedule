package com.rugby.parser.infrastructure.kafka;

import com.rugby.parser.domain.ports.MessageProducer;
import java.util.List;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service("KafkaProducer")
public class KafkaMessageProducerImpl implements MessageProducer {
    private static final Logger LOGGER = Logger.getLogger(KafkaMessageProducerImpl.class.getName());
    private final KafkaProducer<String, String> producer;

    @Override
    public void sendMessage(List<String> messageList) {
        ProducerRecord<String, String> record = new ProducerRecord<>("rugby", "", createMessage(messageList));
        producer.send(record, (metadata, exception) -> {
            if (exception != null) {
                System.err.println("Error sending message: " + exception.getMessage());
            } else {
                System.out.println("Message sent successfully: " + metadata.toString());
            }
        });
    }

    private String createMessage(List<String> messageList) {
        if (messageList.isEmpty())
            return Strings.EMPTY;

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(messageList.get(0));

        for (int i = 1; i < messageList.size(); i++ )
            messageBuilder.append("\n").append(i).append(". ").append(messageList.get(i));

        return messageBuilder.toString();
    }

    @Override
    public void disconnect() {
        throw new NotImplementedException();
    }
}
