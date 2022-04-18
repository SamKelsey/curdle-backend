package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetColourTask;
import org.springframework.stereotype.Service;

import java.awt.*;

import static io.github.samkelsey.wordzle.model.GameStatus.LOST;
import static io.github.samkelsey.wordzle.model.GameStatus.WON;

@Service
public class GuessService {

    private final ResetTargetColourTask resetTargetColourTask;

    public GuessService(ResetTargetColourTask resetTargetColourTask) {
        this.resetTargetColourTask = resetTargetColourTask;
    }

    public ResponseDto makeGuess(UserData userData, RequestDto dto) {

        if (isGameOver(userData)) {
            return new ResponseDto(userData);
        }

        Guess guessResult = evaluateGuess(dto);

        userData.setLives(userData.getLives() - 1);
        userData.getGuesses().add(guessResult);

        if (isCorrectGuess(guessResult)) {
            userData.setGameStatus(WON);
        } else if (userData.getLives() <= 0) {
            userData.setGameStatus(LOST);
        }

        return new ResponseDto(
                isCorrectGuess(guessResult),
                userData
        );
    }

    private Guess evaluateGuess(RequestDto dto) {
        Color color = new Color(dto.getRed(), dto.getGreen(), dto.getBlue());
        Color targetColor = resetTargetColourTask.getTargetColour();

        /*

        Distance Formula: ((r2 - r1)^2 + (g2 - g1)^2 + (b2 - b1)^2)^1/2

        int distance = (int) Math.sqrt(
               Math.pow(color.getRed() - targetColor.getRed(), 2) +
               Math.pow(color.getGreen() - targetColor.getGreen(), 2) +
               Math.pow(color.getBlue() - targetColor.getBlue(), 2));

        */

        /*  Percentage difference = (((r2 - r1)/255 + (g2 - g1)/255 + (b2 - b1))/255)/3 * 100   */
        float diffRed = Math.abs(color.getRed() - targetColor.getRed()) / 255f;
        float diffGreen = Math.abs(color.getGreen() - targetColor.getGreen()) / 255f;
        float diffBlue = Math.abs(color.getBlue() - targetColor.getBlue()) / 255f;
        float avgDiff = ((diffRed + diffGreen + diffBlue) / 3) * 100f;


        return new Guess(color, Math.round(100 - avgDiff));
    }

    private boolean isGameOver(UserData userData) {
        return userData.getGameStatus() == LOST || userData.getGameStatus() == WON;
    }

    private boolean isCorrectGuess(Guess guess) {
        return guess.getAccuracy() > 90;
    }
}
