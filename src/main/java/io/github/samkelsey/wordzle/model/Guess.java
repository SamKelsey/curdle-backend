package io.github.samkelsey.wordzle.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
public class Guess implements Serializable {

    private Color guess;
    private int accuracy;

}
