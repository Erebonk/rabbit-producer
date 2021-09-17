package com.ere.rabbit.documentProducer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Owner info
 * @version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Owner implements Serializable {

    @JsonProperty("name")
    private String name;

    @JsonProperty("surname")
    private String surname;

}
