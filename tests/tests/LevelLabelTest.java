package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lattice.Label;
import lattice.LevelLabel;

public class LevelLabelTest {

    @Test
    public void flowsToBehavior() {
        assertTrue(LevelLabel.LOW.flowsTo(LevelLabel.LOW));
        assertTrue(LevelLabel.LOW.flowsTo(LevelLabel.HIGH));

        assertFalse(LevelLabel.HIGH.flowsTo(LevelLabel.LOW));
        assertTrue(LevelLabel.HIGH.flowsTo(LevelLabel.HIGH));
    }

    @Test
    public void joinBehavior() {
        Label lowLow = LevelLabel.LOW.join(LevelLabel.LOW);
        Label lowHigh = LevelLabel.LOW.join(LevelLabel.HIGH);
        Label highLow = LevelLabel.HIGH.join(LevelLabel.LOW);
        Label highHigh = LevelLabel.HIGH.join(LevelLabel.HIGH);

        assertEquals(LevelLabel.LOW, lowLow);
        assertEquals(LevelLabel.HIGH, lowHigh);
        assertEquals(LevelLabel.HIGH, highLow);
        assertEquals(LevelLabel.HIGH, highHigh);
    }
}
