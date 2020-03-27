package com.ere.rabbit.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.context.annotation.Configuration;

/**
 * Logger config for controller
 *
 * @author ilya
 * @version 1.0
 */
@Slf4j
@Aspect
@Configuration
public class LoggerConfig {

    @Around(value = "@annotation(com.ere.rabbit.producer.domain.annotation.EventsLogger)")
    public Object aroundGetRequest(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        log.info(String.format("request with id %s add to queue: ", proceedingJoinPoint.getArgs()));
        var proceed = proceedingJoinPoint.proceed();

        log.info("returned value: " + proceedingJoinPoint.getSignature());

        return proceed;
    }

}
