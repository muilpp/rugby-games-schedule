package com.rugby.parser.infrastructure.mqtt;

import com.rugby.parser.domain.ports.MessageProducer;
import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.logging.Logger;

@Service("MqttProducer")
public class MqttMessageProducerImpl implements MessageProducer {

    private static final Logger LOGGER = Logger.getLogger(MqttMessageProducerImpl.class.getName());
    private final IMqttClient mqttClient;

    public MqttMessageProducerImpl(IMqttClient mqttClient) {
        this.mqttClient = mqttClient;
    }

    @Override
    public void sendMessage(List<String> messageList) {
        if ( !mqttClient.isConnected()) {
            return;
        }

        MqttMessage msg = createMessage(messageList);
        msg.setQos(2);
        msg.setRetained(false);

        try {
            mqttClient.publish("rugby",msg);
        } catch (MqttException e) {
            LOGGER.severe("Can't send message, reason: " + e.getMessage());
        }
    }

    @Override
    public void disconnect() {
        if (mqttClient.isConnected()) {
            try {
                mqttClient.disconnect();
            } catch (MqttException e) {
                LOGGER.severe("Can't disconnect client: " + e.getMessage());
            }
        }
    }

    private MqttMessage createMessage(List<String> messageList) {
        if (messageList.isEmpty())
            return new MqttMessage();

        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(messageList.get(0));

        for (int i = 1; i < messageList.size(); i++ )
            messageBuilder.append("\n").append(i).append(". ").append(messageList.get(i));

        byte[] payload = messageBuilder.toString().getBytes(StandardCharsets.UTF_8);
        return new MqttMessage(payload);
    }
}
