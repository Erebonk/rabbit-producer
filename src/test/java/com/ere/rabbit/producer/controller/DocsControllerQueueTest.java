package com.ere.rabbit.producer.controller;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.domain.Owner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Docs multithreaded test
 *
 *  Warning -> you must use parallel run
 *
 * @author ilya
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DocsControllerQueueTest {

    TestRestTemplate testRestTemplate;
    List<InfoDocument> documentLists;

    String url;

    HttpHeaders httpHeaders;

    @BeforeEach
    void init() {
        url = "http://localhost:8070docs";
        httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        testRestTemplate = new TestRestTemplate();

        documentLists = new ArrayList<>();
        for (int i = 0; i < 2500; i++) {
            documentLists.add(new InfoDocument(String.valueOf(i), "test_" + i,
                    new Owner("Mice", "OfCourse")));
        }
    }

    @Test
    void shouldAddDocsToQueue() {
        documentLists.forEach(infoDocument ->
                this.testRestTemplate.postForEntity(url, new HttpEntity<>(infoDocument, httpHeaders), String.class));
    }

    @Test
    void shouldAddDocsWithMultiTreads() throws InterruptedException {
        var executor = Executors.newFixedThreadPool(12);
        Runnable firstExecutor = () -> {
            for (int i = 0; i < 2750; i++) {
                this.testRestTemplate.postForEntity(url,
                        new HttpEntity<>(
                                new InfoDocument("id1_" + i, "docs1" + i,
                                        new Owner("Kate", "Lucas")), httpHeaders), String.class);
            }
        };
        executor.submit(firstExecutor);

        Runnable secondExecutor = () -> {
            for (int i = 0; i < 3498; i++) {
                this.testRestTemplate.postForEntity(url,
                        new HttpEntity<>(
                                new InfoDocument("id2_" + i, "docs2" + i,
                                        new Owner("Max", "Threadov")), httpHeaders), String.class);
            }
        };
        executor.submit(secondExecutor);

        Runnable thirdExecutor = () -> {
            for (int i = 0; i < 5423; i++) {
                this.testRestTemplate.postForEntity(url,
                        new HttpEntity<>(
                                new InfoDocument("id3_" + i, "docs3" + i,
                                        new Owner("Ivan", "Tesovitch")), httpHeaders), String.class);
            }
        };
        executor.submit(thirdExecutor);

        executor.shutdown();
        while (executor.awaitTermination(3, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }
        executor.shutdownNow();
    }

    @Test
    void getInfo() {
        throw new RuntimeException();
    }
}