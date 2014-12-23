package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

public class SpeedTest {

    @Test
    public void testGet() throws Exception {
        Speed theSpeed = new Speed(28);
        assertEquals(28L, theSpeed.get(), 0);
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