package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.*;

public class CustomPropertiesTest {

    @Test
    public void testGetClassInformation() throws Exception {
        CustomProperties theProperties = new CustomProperties();
        assertTrue(theProperties.getClassInformation() instanceof CustomPropertiesClassInformation);
    }

    @Test
    public void testSet() throws Exception {
        CustomProperties theProperties = new CustomProperties();
        assertFalse(theProperties.has("key"));
        theProperties.set("key", "value");
        assertTrue(theProperties.has("key"));
        assertEquals("value", theProperties.get("key"));
        theProperties.set("key", "value1");
        assertEquals("value1", theProperties.get("key"));
        theProperties.set("key", null);
        assertNull(theProperties.get("key"));
        assertFalse(theProperties.has("key"));
    }

    @Test
    public void testSerialize() throws Exception {
        CustomProperties theProperties = new CustomProperties();
        theProperties.set("key", "value");
        Map<String, Object> theData = theProperties.serialize();
        assertEquals(1, theData.size(), 0);
        assertEquals("value", theProperties.get("key"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("key", "value");
        CustomProperties theProperties = CustomProperties.deserialize(theData);
        assertTrue(theProperties.has("key"));
        assertEquals("value", theProperties.get("key"));
    }
}