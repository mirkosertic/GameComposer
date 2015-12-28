package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ColorTest {

    @Test
    public void testEquals() {
        Color theColor1 = new Color(1,2,3);
        Color theColor2 = new Color(1,2,3);
        assertTrue(theColor1.equals(theColor1));
        assertTrue(theColor1.equals(theColor2));
        assertFalse(theColor1.equals(Color.BLACK));
        assertFalse(theColor1.equals(null));
        assertFalse(theColor1.equals(1));
    }

    @Test
    public void testHashCode() {
        Color theColor1 = new Color(1,2,3);
        Color theColor2 = new Color(1,2,4);
        assertTrue(theColor1.hashCode() == theColor2.hashCode());
        assertFalse(theColor1.hashCode() == Color.WHITE.hashCode());
    }

    @Test
    public void testBlack() {
        Color theColor = Color.BLACK;
        assertEquals(0, theColor.r);
        assertEquals(0, theColor.g);
        assertEquals(0, theColor.b);
    }

    @Test
    public void testWhite() {
        Color theColor = Color.WHITE;
        assertEquals(255, theColor.r);
        assertEquals(255, theColor.g);
        assertEquals(255, theColor.b);
    }

    @Test
    public void testGetClassInformation() throws Exception {
        Color theColor = Color.WHITE;
        assertTrue(theColor.getClassInformation() instanceof ColorClassInformation);
    }

    @Test
    public void testSerialize() throws Exception {
        Color theColor = new Color(10, 20, 30);
        assertEquals(10, theColor.r);
        assertEquals(20, theColor.g);
        assertEquals(30, theColor.b);
        Map<String, Object> theData = theColor.serialize();
        assertEquals(3, theData.size());
        assertEquals("10", theData.get("r"));
        assertEquals("20", theData.get("g"));
        assertEquals("30", theData.get("b"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("r", "10");
        theData.put("g", "20");
        theData.put("b", "30");
        Color theColor = Color.deserialize(theData);
        assertEquals(10, theColor.r);
        assertEquals(20, theColor.g);
        assertEquals(30, theColor.b);
    }
}