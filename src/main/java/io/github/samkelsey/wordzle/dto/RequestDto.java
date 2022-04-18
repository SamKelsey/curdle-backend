package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @NotNull(message = "red is a mandatory field.")
    private Integer red;

    @NotNull(message = "green is a mandatory field.")
    private Integer green;

    @NotNull(message = "blue is a mandatory field.")
    private Integer blue;

}
