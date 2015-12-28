package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class SpeedTest {

    @Test
    public void testGet() throws Exception {
        Speed theSpeed = new Speed(28);
        assertEquals(28L, theSpeed.get(), 0);
    }

    @Test
    public void testIncrementBy() {
        Speed theSpeed = new Speed(20);
        Speed theOtherSpeed = theSpeed.incrementBy(10);
        assertEquals(30L, theOtherSpeed.get(), 0);
        assertNotSame(theSpeed, theOtherSpeed);
    }

    @Test
    public void testResetToZero() {
        Speed theSpeed = new Speed(10);
        Speed theOtherSpeed = theSpeed.resetToZero();
        assertEquals(0l, theOtherSpeed.get(), 0);
        assertNotSame(theSpeed, theOtherSpeed);
    }

    @Test
    public void testSerialize() throws Exception {
        Speed theSpeed = new Speed(28);
        Map<String, Object> theData = theSpeed.serialize();
        assertEquals(1, theData.size());
        assertEquals("28", theData.get("speed"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("speed", "28");
        Speed theSpeed = Speed.deserialize(theData);
        assertEquals(28L, theSpeed.get(), 0);
    }
}