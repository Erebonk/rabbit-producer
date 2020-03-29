package com.ere.rabbit.producer.service;

import com.ere.rabbit.producer.domain.InfoDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Processing service with rabbit mq
 *
 * @author ilya
 * @version 1.1
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QueueProcessingService {

    private final RabbitTemplate rabbitTemplate;

    private ConcurrentLinkedQueue<InfoDocument> infoDocuments = new ConcurrentLinkedQueue<>();
    private volatile LocalDateTime timeStump;

    public void addToQueue(InfoDocument document) {
        if (infoDocuments.size() >= 1000) {
            pushToQueue();
            infoDocuments.clear();
        } else
            infoDocuments.offer(document);
    }

    public int queueSize() {
        return infoDocuments.size();
    }

    public LocalDateTime getTimeStump() {
        return timeStump;
    }

    public void pushToQueue() {
        rabbitTemplate.convertAndSend("document-queue-saved", infoDocuments);
        timeStump = LocalDateTime.now();
    }

}
