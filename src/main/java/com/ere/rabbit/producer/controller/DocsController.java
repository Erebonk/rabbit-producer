package com.ere.rabbit.producer.controller;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.domain.annotation.EventsLogger;
import com.ere.rabbit.producer.service.QueueProcessingService;
import com.ere.rabbit.producer.service.RabbitDocsInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Product controller
 *
 * @author ilya
 * @version 1.2
 */
@Slf4j
@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final QueueProcessingService queueProcessingService;
    private final RabbitDocsInfoService rabbitDocsInfoService;

    @PostMapping
    public ResponseEntity<?> addDocument(@RequestBody InfoDocument infoDocument) {
        queueProcessingService.addToQueue(infoDocument);
        log.info("accepted: " + infoDocument);
        return ResponseEntity.accepted().body("accepted: " + infoDocument.getId());
    }

    @GetMapping
    @EventsLogger
    public ResponseEntity<?> getInfo(@RequestParam("id") String id) {
        return ResponseEntity.ok().body(rabbitDocsInfoService.getInfoDocsResult(id));
    }

}
