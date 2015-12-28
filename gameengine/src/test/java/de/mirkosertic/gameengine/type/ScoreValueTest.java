package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public class ScoreValueTest {

    @Test
    public void testInitDefault() {
        ScoreValue theValue = new ScoreValue();
        assertEquals(0l, theValue.get().longValue());
    }

    @Test
    public void testIncrement() {
        ScoreValue theValue = new ScoreValue(10);
        ScoreValue theNewValue = theValue.incrementBy(10);
        assertEquals(20, theNewValue.get(), 0);
        assertNotSame(theValue, theNewValue);
    }

    @Test
    public void testResetToZero() {
        ScoreValue theValue = new ScoreValue(10);
        ScoreValue theNewValue = theValue.resetToZero();
        assertEquals(0, theNewValue.get(), 0);
        assertNotSame(theValue, theNewValue);
    }

    @Test
    public void testInitWithValue() {
        ScoreValue theValue = new ScoreValue(13);
        assertEquals(13l, theValue.get().longValue());
    }

    @Test
    public void testEquals() throws Exception {
        ScoreValue theScoreValue = new ScoreValue(1);
        ScoreValue theScoreValue1 = new ScoreValue(1);
        ScoreValue theScoreValue2 = new ScoreValue(2);
        assertEquals(theScoreValue, theScoreValue);
        assertEquals(theScoreValue, theScoreValue1);
        assertNotEquals(theScoreValue, theScoreValue2);
        assertNotEquals(theScoreValue, null);
        assertNotEquals(theScoreValue, "lala");
    }

    @Test
    public void testHashCode() throws Exception {
        ScoreValue theScoreValue = new ScoreValue(-84545);
        assertEquals(84544, theScoreValue.hashCode());
    }

    @Test
    public void testSerialize() throws Exception {
        ScoreValue theValue = new ScoreValue(13);
        Map<String, Object> theData = theValue.serialize();
        assertEquals(1, theData.size());
        assertEquals("13", theData.get("score"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("score", "13");
        ScoreValue theValue = ScoreValue.deserialize(theData);
        assertEquals(13L, theValue.get().longValue());
    }
}