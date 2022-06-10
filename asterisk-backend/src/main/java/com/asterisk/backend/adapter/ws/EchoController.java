package com.asterisk.backend.adapter.ws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class EchoController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoController.class);

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public String greeting(final String message) throws Exception {
        LOGGER.info("New message received: " + message);
        return message;
    }
}
