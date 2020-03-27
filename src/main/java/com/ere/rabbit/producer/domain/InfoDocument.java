package com.ere.rabbit.producer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Info document
 *
 * @author ilya
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class InfoDocument implements Serializable {

    @JsonProperty("id")
    private String id;

    @JsonProperty("info")
    private String info;

}
