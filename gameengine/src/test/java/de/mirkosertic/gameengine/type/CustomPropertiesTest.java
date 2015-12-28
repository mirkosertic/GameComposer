package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
}