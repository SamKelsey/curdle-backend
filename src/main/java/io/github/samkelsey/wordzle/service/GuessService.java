package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.dto.RGB;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.schedule.ResetTargetColourTask;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Comparator;

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
            return new ResponseDto(userData, resetTargetColourTask.getTargetColour());
        }

        Guess guessResult = evaluateGuess(dto);
        updateUserData(userData, guessResult);

        return new ResponseDto(
                isCorrectGuess(guessResult),
                userData,
                resetTargetColourTask.getTargetColour()
        );
    }

    private void updateUserData(UserData userData, Guess guess) {
        userData.setLives(userData.getLives() - 1);
        userData.getGuesses().add(guess);
        userData.setBestGuess(
                userData.getGuesses().stream()
                        .max(Comparator.comparingInt(Guess::getAccuracy)).get()
        );

        if (isCorrectGuess(guess)) {
            userData.setGameStatus(WON);
        } else if (userData.getLives() <= 0) {
            userData.setGameStatus(LOST);
        }
    }

    private Guess evaluateGuess(RequestDto dto) {
        RGB color = calculateGuessProduct(dto);
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


        return new Guess(dto.getColour1(), dto.getColour2(), color, Math.round(100 - avgDiff));
    }

    /*  Takes avg between 2 colors rgb values.  */
    private RGB calculateGuessProduct(RequestDto dto) {
        return new RGB(
                (dto.getColour1().getRed() + dto.getColour2().getRed())/2,
                (dto.getColour1().getGreen() + dto.getColour2().getGreen())/2,
                (dto.getColour1().getBlue() + dto.getColour2().getBlue())/2
        );
    }

    private boolean isGameOver(UserData userData) {
        return userData.getGameStatus() == LOST || userData.getGameStatus() == WON;
    }

    private boolean isCorrectGuess(Guess guess) {
        return guess.getAccuracy() > 90;
    }
}
