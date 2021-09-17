package com.ere.rabbit.documentProducer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for get info about document
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class RabbitDocsInfoService {

    private final RabbitTemplate rabbitTemplate;

    public Object getInfoDocsResult(String id) {
        return rabbitTemplate.convertSendAndReceive("document-queue-find", id);
    }
}
