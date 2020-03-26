package com.ere.rabbit.producer.service;

import com.ere.rabbit.producer.domain.Document;
import com.ere.rabbit.producer.domain.InfoDocument;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProcessingService {

    private final RabbitTemplate rabbitTemplate;

    private ConcurrentLinkedQueue<InfoDocument> infoDocuments = new ConcurrentLinkedQueue<>();

    public void addToQueue(Document document) {
        if (document.getType().equals("info")) {
            if (infoDocuments.size() <= 1000) {
                infoDocuments.offer((InfoDocument) document);
                rabbitTemplate.convertAndSend("document-queue-saved", infoDocuments);
                infoDocuments.clear();
            } else
                infoDocuments.offer((InfoDocument) document);
        }
    }

}
