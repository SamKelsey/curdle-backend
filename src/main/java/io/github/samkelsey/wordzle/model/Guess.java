package io.github.samkelsey.wordzle.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.samkelsey.wordzle.dto.RGB;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Guess implements Serializable {

    @JsonProperty("colour-1")
    private RGB colour1;

    @JsonProperty("colour-2")
    private RGB colour2;

    private RGB guess;

    private int accuracy;

}
