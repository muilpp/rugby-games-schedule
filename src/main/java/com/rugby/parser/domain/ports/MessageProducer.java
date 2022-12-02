package com.rugby.parser.domain.ports;

import java.util.List;

public interface MessageProducer {

    void sendMessage(List<String> messageList);
    void disconnect();
}
