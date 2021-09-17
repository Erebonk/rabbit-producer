package com.ere.rabbit.documentProducer.controller;

import com.ere.rabbit.documentProducer.domain.InfoDocument;
import com.ere.rabbit.documentProducer.domain.Owner;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Docs controller test for rabbit mq
 *
 * @author ilya
 * @version 1.0
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class DocsControllerTest {

    @Autowired
    MockMvc mockMvc;

    String url = "/docs";

    InfoDocument doc;

    @BeforeEach
    void init() {
        doc = new InfoDocument();
        doc.setUid("1");
        doc.setOwner(new Owner("Bar", "Barkov"));
        doc.setInfo("new document");
    }

    @Test
    void shouldPostNewDocToQueue() throws Exception {
        this.mockMvc.perform(
                MockMvcRequestBuilders.post(url)
                        .content(new ObjectMapper().writeValueAsString(doc))
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(content().string(containsString("accepted: ")));
    }

}