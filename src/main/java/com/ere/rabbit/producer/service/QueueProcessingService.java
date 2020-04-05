package com.ere.rabbit.producer.service;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.domain.annotation.PushTimeStamp;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Processing service with rabbit mq
 *
 * @author ilya
 * @version 1.3
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class QueueProcessingService {

    private final RabbitTemplate rabbitTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    private ConcurrentLinkedQueue<InfoDocument> infoDocumentsQueue = new ConcurrentLinkedQueue<>();

    public void addToQueue(InfoDocument document) throws JsonProcessingException {
        redisTemplate.opsForHash()
                .put(
                        String.join("/", document.getOwner().toString(), document.getUid()),
                        String.valueOf(document.hashCode()),
                        new ObjectMapper().writeValueAsString(document));
        if (infoDocumentsQueue.size() >= 1000) {
            pushToQueue();
            infoDocumentsQueue.clear();
        }
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
