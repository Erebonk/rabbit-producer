package com.ere.rabbit.documentProducer.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * info document
 ** @version 1.1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class InfoDocument implements Serializable {

    @JsonProperty("uid")
    private String uid;

    @JsonProperty("info")
    private String info;

    @NotNull
    @JsonProperty("owner")
    private Owner owner;

}
