package com.ere.rabbit.producer.config;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.CollectionLikeType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Rabbit configuration
 *
 * @author ilya
 * @version 1.0
 */
@EnableRabbit
@Configuration
public class RabbitConfig {

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory("localhost");
        factory.setConnectionTimeout(60 * 1000);
        factory.setCloseTimeout(60 * 1000);
        return factory;
    }

    @Bean
    public RabbitTemplate rabbitTemplate() {
        var rabbitTemplate = new RabbitTemplate(connectionFactory());
        rabbitTemplate.setReplyTimeout(60 * 1000);
        return rabbitTemplate;
    }

    @Bean
    public AmqpAdmin amqpAdmin() {
        return new RabbitAdmin(connectionFactory());
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("document-topic");
    }

    @Bean
    public Queue documentQueue() {
        return new Queue("document-queue-saved");
    }

    @Bean
    public Queue findDocQueue() {
        return new Queue("document-queue-find");
    }

//    @Bean
//    public MessageConverter converter() {
//        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
//        converter.setJavaTypeMapper(new DefaultJackson2JavaTypeMapper() {
//
//            @Override
//            public JavaType toJavaType(MessageProperties properties) {
//                JavaType javaType = super.toJavaType(properties);
//                if (javaType instanceof CollectionLikeType) {
//                    return TypeFactory.defaultInstance()
//                            .constructCollectionLikeType(ConcurrentLinkedQueue.class, InfoDocument.class);
//                }
//                else {
//                    return javaType;
//                }
//            }
//
//        });
//        return converter;
//    }

}
