package com.ere.rabbit.documentProducer.controller;

import com.ere.rabbit.documentProducer.domain.InfoDocument;
import com.ere.rabbit.documentProducer.domain.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

/**
 * Docs multithreaded test
 *
 *  Warning -> you must use parallel run
 *
 * @author ilya
 * @version 1.1
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocsControllerIntegrationTestQueueTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    String url;

    HttpHeaders httpHeaders;

    List<InfoDocument> firstList;
    List<InfoDocument> secondList;
    List<InfoDocument> thirdList;

    Owner pOwner;
    Owner sOwner;
    Owner dOwner;

    BiFunction<String, Owner, InfoDocument> documentFunctional = (docsPrefix, owner) ->
            new InfoDocument(String.valueOf(UUID.randomUUID()), docsPrefix, owner);

    @BeforeEach
    void init() {
        httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        url = "http://localhost:8070/docs";

        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        pOwner = new Owner("Ivan", "Ivanov");
        sOwner = new Owner("Bob", "Maxs");
        dOwner = new Owner("Uoran", "Basket");

        firstList = IntStream.range(1, 2450)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), pOwner))
                .collect(toList());
        secondList = IntStream.range(1, 32432)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), sOwner))
                .collect(toList());
        thirdList = IntStream.range(1, 54123)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), dOwner))
                .collect(toList());
    }

    @Test
    void shouldAddDocsWithMultiTreads() throws Exception {
        var executor = Executors.newFixedThreadPool(100);

        Callable<String> firstExecutor = () -> {
            TestRestTemplate firstRestTemp = new TestRestTemplate();
            this.firstList.forEach(doc -> firstRestTemp.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(doc, httpHeaders), String.class));
            return "OK";
        };
        executor.submit(firstExecutor);

        Callable<String> secondExecutor = () -> {
            TestRestTemplate secondRestTemp = new TestRestTemplate();
            this.secondList.forEach(doc -> secondRestTemp.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(doc, httpHeaders), String.class));
            return "OK";
        };
        executor.submit(secondExecutor);

        Callable<String> thirdExecutor = () -> {
            TestRestTemplate thirdRestTemp = new TestRestTemplate();
            this.thirdList.forEach(doc -> thirdRestTemp.exchange(url, HttpMethod.POST,
                    new HttpEntity<>(doc, httpHeaders), String.class));
            return "OK";
        };
        executor.submit(thirdExecutor);

        executor.shutdown();
        if (!executor.awaitTermination(3, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }
    }

    @Test
    void getInfo() {
        throw new RuntimeException();
    }
}