package io.github.samkelsey.wordzle.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class RGB implements Serializable {

    @NotNull(message = "red is a mandatory field.")
    private Integer red;

    @NotNull(message = "green is a mandatory field.")
    private Integer green;

    @NotNull(message = "blue is a mandatory field.")
    private Integer blue;

}
