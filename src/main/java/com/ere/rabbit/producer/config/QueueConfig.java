package com.ere.rabbit.producer.config;

import com.ere.rabbit.producer.service.ProcessingService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Aspect
@Component
@EnableScheduling
@RequiredArgsConstructor
public class QueueConfig {

    private final ProcessingService processingService;

    @Scheduled(cron = "${settings.cron.info}")
    public void sendInfoDocs() {
        if (processingService.getInfoDocsQueueSize() != 0 && processingService.getInfoDocsQueueSize() < 1000) {

        }
    }

}
