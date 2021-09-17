package com.ere.rabbit.documentProducer.controller;

import com.ere.rabbit.documentProducer.domain.InfoDocument;
import com.ere.rabbit.documentProducer.domain.annotation.EventsLogger;
import com.ere.rabbit.documentProducer.service.QueueProcessingService;
import com.ere.rabbit.documentProducer.service.RabbitDocsInfoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Product controller
 * @version 1.3
 */
@Slf4j
@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final QueueProcessingService queueProcessingService;
    private final RabbitDocsInfoService rabbitDocsInfoService;

    @PostMapping
    public ResponseEntity<String> addDocument(@RequestBody InfoDocument infoDocument) throws JsonProcessingException {
        queueProcessingService.addToQueue(infoDocument);
        log.info("accepted: " + infoDocument);
        return ResponseEntity.accepted().body("accepted: " + infoDocument.getUid());
    }

    @GetMapping
    @EventsLogger
    public ResponseEntity<?> getInfo(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(rabbitDocsInfoService.getInfoDocsResult(id));
    }

}
