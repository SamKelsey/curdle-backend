package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RequestDto {

    @Valid
    @NotNull(message = "colour1 is a mandatory field.")
    @JsonProperty("colour-1")
    private RGB colour1;

    @Valid
    @NotNull(message = "colour2 is a mandatory field.")
    @JsonProperty("colour-2")
    private RGB colour2;

}
