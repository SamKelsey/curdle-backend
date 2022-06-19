package io.github.samkelsey.wordzle.service;

import io.github.samkelsey.wordzle.dto.RGB;
import io.github.samkelsey.wordzle.model.GameStatus;
import io.github.samkelsey.wordzle.TestUtils;
import io.github.samkelsey.wordzle.model.Guess;
import io.github.samkelsey.wordzle.model.UserData;
import io.github.samkelsey.wordzle.dto.RequestDto;
import io.github.samkelsey.wordzle.dto.ResponseDto;
import io.github.samkelsey.wordzle.schedule.ResetTargetColourTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static io.github.samkelsey.wordzle.model.GameStatus.LOST;
import static io.github.samkelsey.wordzle.model.GameStatus.WON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GuessServiceTest {

    @Mock
    private ResetTargetColourTask resetTargetWordTask;

    @InjectMocks
    private GuessService guessService;

    private RequestDto dto;
    private UserData userData;

    @BeforeEach
    public void init() {
        dto = TestUtils.createSampleRequestDto();
        userData = TestUtils.createSampleUserData();
    }

    @Test
    void whenCorrectGuess_shouldGameOver() {
        when(resetTargetWordTask.getTargetColour()).thenReturn(new Color(dto.getColour1().getRed(), dto.getColour1().getGreen(), dto.getColour1().getBlue()));

        ResponseDto responseDto = guessService.makeGuess(userData, dto);

        assertEquals(WON, responseDto.getGameStatus());
    }

    @Test
    void whenMakeGuess_shouldDeductLife() {
        when(resetTargetWordTask.getTargetColour()).thenReturn(new Color(dto.getColour1().getRed(), dto.getColour1().getGreen(), dto.getColour1().getBlue()));
        int initialLives = userData.getLives();

        int lives = guessService.makeGuess(userData, dto).getLives();

        assertEquals(initialLives, lives + 1);
    }

    @Test
    void whenMakeGuess_shouldEvaluateGuess() {
        RequestDto dto = new RequestDto(
                new RGB(245, 250, 255),
                new RGB(245, 250, 255)
        );
        when(resetTargetWordTask.getTargetColour()).thenReturn(new Color(255, 255, 255));

        List<Guess> guesses = guessService.makeGuess(userData, dto).getGuesses();

        Guess result = guesses.get(guesses.size() - 1);
        Guess expected = new Guess(
                new RGB(245, 250, 255),
                new RGB(245, 250, 255),
                new RGB(245, 250, 255),
                98
        );
        assertEquals(expected.getGuess().getRed(), result.getGuess().getRed());
        assertEquals(expected.getGuess().getGreen(), result.getGuess().getGreen());
        assertEquals(expected.getGuess().getBlue(), result.getGuess().getBlue());
        assertEquals(expected.getAccuracy(), result.getAccuracy());
    }

    @Test
    void whenMakeGuess_shouldAddGuess() {
        when(resetTargetWordTask.getTargetColour()).thenReturn(TestUtils.jFixture.create(Color.class));
        List<Guess> initialGuesses = new ArrayList<>(userData.getGuesses());

        List<Guess> guesses = guessService.makeGuess(userData, dto).getGuesses();

        assertEquals(initialGuesses.size() + 1, guesses.size());
    }

    @Test
    void whenOutOfLives_shouldGameOver() {
        userData.setLives(0);
        when(resetTargetWordTask.getTargetColour()).thenReturn(new Color(14, 52, 12));

        ResponseDto response = guessService.makeGuess(userData, dto);

        assertEquals(LOST, response.getGameStatus());
    }

    @ParameterizedTest
    @EnumSource(value = GameStatus.class, names = {"WON", "LOST"})
    void whenGameOver_shouldReturnOnlyUserData(GameStatus gameStatus) {
        userData.setGameStatus(gameStatus);

        ResponseDto response = guessService.makeGuess(userData, dto);

        assertNull(response.getGuessIsCorrect());
    }
}
