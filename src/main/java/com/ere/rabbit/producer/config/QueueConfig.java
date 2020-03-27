package com.ere.rabbit.producer.config;

import com.ere.rabbit.producer.service.QueueProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Queue control config
 *
 * @author ilya
 * @version 1.0
 */
@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class QueueConfig {

    private final QueueProcessingService queueProcessingService;

    @Scheduled(cron = "${settings.cron.info}")
    public void pushQueueToRabbit() {
        if (queueProcessingService.queueSize() > 0)
            queueProcessingService.pushToQueue();
    }

}
