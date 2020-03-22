package com.ere.rabbit.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Product controller
 *
 * @author ilya
 * @version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/docs")
@RequiredArgsConstructor
public class DocsController {

    private final RabbitTemplate rabbitTemplate;
    private final AtomicInteger atomicInteger = new AtomicInteger(0);

    @PostMapping
    public ResponseEntity<?> saveDoc() {
        log.info("emit to queue");
        rabbitTemplate.convertAndSend("document-queue-saved", atomicInteger.incrementAndGet());
        log.info("emit to queue: " + atomicInteger.get());
        return ResponseEntity.accepted().body("emit to queue");
    }

    @GetMapping
    public ResponseEntity<?> getInfo(@RequestParam("id") String id) {
        log.info("emit to queue");
        var response = rabbitTemplate.convertSendAndReceive("document-queue-find", id);
        log.info("response: " + response);
        return ResponseEntity.accepted().body(response);
    }

}
