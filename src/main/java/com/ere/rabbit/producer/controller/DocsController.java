package com.ere.rabbit.producer.controller;

import com.ere.rabbit.producer.domain.Document;
import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Product controller
 *
 * @author ilya
 * @version 1.1
 */
@Slf4j
@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final ProcessingService processingService;

    private ConcurrentHashMap<String, Document> concurrentHashMap = new ConcurrentHashMap<>();

    @PostMapping
    public ResponseEntity<?> addDocument(@PathVariable InfoDocument infoDocument) {
        concurrentHashMap.put(infoDocument.getType(), infoDocument);
        processingService.addToQueue(infoDocument);
        return ResponseEntity.accepted().body("accepted: " + infoDocument.getId());
    }

//    @GetMapping
//    public ResponseEntity<?> getInfo(@RequestParam("id") String id) {
//        log.info("emit to queue");
//        var response = rabbitTemplate.convertSendAndReceive("document-queue-find", id);
//        log.info("response: " + response);
//        return ResponseEntity.accepted().body(response);
//    }

}
