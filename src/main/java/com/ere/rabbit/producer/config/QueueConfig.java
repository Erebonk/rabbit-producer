package com.ere.rabbit.producer.config;

import com.ere.rabbit.producer.service.QueueProcessingService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

/**
 * Queue control config
 *
 * @author ilya
 * @version 1.0
 */
@Aspect
@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class QueueConfig {

    private final QueueProcessingService queueProcessingService;

    private LocalDateTime pushTime;

    @AfterReturning(value = "@annotation(com.ere.rabbit.producer.domain.annotation.PushTimeStamp)")
    public void proceedPushQueueEvent() {
        pushTime = LocalDateTime.now();
    }

    @Scheduled(cron = "${settings.cron.info}")
    public void pushQueueToRabbit() {
        if (pushTime.plusMinutes(5).isBefore(LocalDateTime.now()))
            if (queueProcessingService.queueSize() > 0)
                queueProcessingService.pushToQueue();
    }

}
