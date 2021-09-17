package com.ere.rabbit.documentProducer.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Rabbit configuration
 * @version 1.1
 */
@EnableRabbit
@Configuration
public class RabbitConfig {

    private final int connectionTimeout = 60 * 1000;
    private final int closeTimeout = 60 * 1000;
    private final String host = "localhost";
    private final String topicExchange = "document-topic";
    private final String docQueue = "document-queue-saved";
    private final String findQueue = "document-queue-find";

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(host);
        factory.setConnectionTimeout(connectionTimeout);
        factory.setCloseTimeout(closeTimeout);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        var rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyTimeout(60 * 1000);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(topicExchange);
    }

    @Bean
    public Queue documentQueue() {
        return new Queue(docQueue);
    }

    @Bean
    public Queue findDocQueue() { return new Queue(findQueue); }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
