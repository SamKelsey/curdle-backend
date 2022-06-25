package io.github.samkelsey.wordzle.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.samkelsey.wordzle.model.GameStatus;
import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import lombok.Getter;
import lombok.Setter;

import java.awt.*;
import java.util.List;

@Getter
@Setter
@JsonInclude(Include.NON_NULL)
public class ResponseDto {

    @JsonProperty("game-status")
    private final GameStatus gameStatus;

    @JsonProperty("guess-is-correct")
    private Boolean guessIsCorrect;

    @JsonProperty("target-colour")
    private RGB targetColour;

    private List<Guess> guesses;

    private int lives;

    @JsonProperty("best-guess")
    private Guess bestGuess;

    public ResponseDto(UserData userData, Color targetColour) {
        this.gameStatus = userData.getGameStatus();
        this.guessIsCorrect = null;
        this.targetColour = new RGB(targetColour.getRed(), targetColour.getGreen(), targetColour.getBlue());
        this.guesses = userData.getGuesses();
        this.lives = userData.getLives();
        this.bestGuess = userData.getBestGuess();
    }

    public ResponseDto(boolean guessIsCorrect, UserData userData, Color targetColour) {
        this.gameStatus = userData.getGameStatus();
        this.guessIsCorrect = guessIsCorrect;
        this.targetColour = new RGB(targetColour.getRed(), targetColour.getGreen(), targetColour.getBlue());
        this.guesses = userData.getGuesses();
        this.lives = userData.getLives();
        this.bestGuess = userData.getBestGuess();
    }
}
