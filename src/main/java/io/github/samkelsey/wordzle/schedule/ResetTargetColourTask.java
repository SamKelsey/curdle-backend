package io.github.samkelsey.wordzle.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.time.Instant;
import java.util.Random;

@Component
public class ResetTargetColourTask {

    Logger LOGGER = LoggerFactory.getLogger(ResetTargetColourTask.class);
    private Color targetColour;
    private long targetColourCreationTime;

    private static final Random rand = new Random();

    @Scheduled(fixedDelayString ="${reset-word-task.delay}")
    public void resetWord() {
        LOGGER.info("Resetting word");
        targetColour = createRandColour();
        targetColourCreationTime = Instant.now().toEpochMilli();
        LOGGER.info("Target colour reset to \"{}\"", targetColour);
    }

    private Color createRandColour() {
        int minBound = 30;
        int maxBound = 225;

        int r = rand.nextInt(maxBound - minBound) + minBound;
        int g = rand.nextInt(maxBound - minBound) + minBound;
        int b = rand.nextInt(maxBound - minBound) + minBound;

        return new Color(r, g, b);
    }

    public Color getTargetColour() {
        return targetColour;
    }

    public long getTargetColourCreationTime() {
        return targetColourCreationTime;
    }
}
