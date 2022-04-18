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
        LOGGER.info("Target word reset to \"{}\"", targetColour);
    }

    private Color createRandColour() {
        int r = rand.nextInt(255);
        int g = rand.nextInt(255);
        int b = rand.nextInt(255);

        return new Color(r, g, b);
    }

    public Color getTargetColour() {
        return targetColour;
    }

    public long getTargetColourCreationTime() {
        return targetColourCreationTime;
    }
}
