package com.ere.rabbit.producer.threadtest;

import com.ere.rabbit.producer.domain.InfoDocument;
import com.ere.rabbit.producer.domain.Owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Mock mvc test
 */
@SpringBootTest
@AutoConfigureMockMvc
class MultithreadingTest {

    @Autowired
    MockMvc mockMvc;

    String url;

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
        url = "http://localhost:8070/docs";

        pOwner = new Owner("Ivan", "Ivanov");
        sOwner = new Owner("Bob", "Maxs");
        dOwner = new Owner("Uoran", "Basket");

        firstList = IntStream.range(1,100)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), pOwner))
                .collect(toList());
        secondList = IntStream.range(1,100)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), sOwner))
                .collect(toList());
        thirdList = IntStream.range(1,100)
                .mapToObj(count -> documentFunctional.apply(String.valueOf(count), dOwner))
                .collect(toList());
    }

    @Test
    void multithreadingTest() throws InterruptedException {
        var executor = Executors.newFixedThreadPool(100);

        Runnable runnableFirst = () -> firstList.forEach(infoDocument -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(new ObjectMapper().writeValueAsString(infoDocument))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isAccepted());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(runnableFirst);

        Runnable runnableSecond = () -> secondList.forEach(infoDocument -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(new ObjectMapper().writeValueAsString(infoDocument))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isAccepted());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(runnableSecond);

        Runnable runnableThird = () -> thirdList.forEach(infoDocument -> {
            try {
                mockMvc.perform(MockMvcRequestBuilders.post(url)
                        .content(new ObjectMapper().writeValueAsString(infoDocument))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                ).andExpect(status().isAccepted());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(runnableThird);

        executor.shutdown();
        while (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            executor.shutdownNow();
        }
    }

}
