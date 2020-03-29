package com.ere.rabbit.producer.service;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.domain.annotation.PushTimeStamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

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

    private ConcurrentLinkedQueue<InfoDocument> infoDocumentsQueue = new ConcurrentLinkedQueue<>();

    public void addToQueue(InfoDocument document) {
        if (infoDocumentsQueue.size() >= 1000) {
            pushToQueue();
            infoDocumentsQueue.clear();
        } else
            infoDocumentsQueue.offer(document);
    }

    public int queueSize() {
        return infoDocumentsQueue.size();
    }

    @PushTimeStamp
    public void pushToQueue() {
        rabbitTemplate.convertAndSend("document-queue-saved", infoDocumentsQueue);
    }

}
