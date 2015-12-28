package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class ResourceNameTest {

    @Test
    public void testInitNull() {
        ResourceName theResourceName = new ResourceName(null);
        assertNull(theResourceName.get());
    }

    @Test
    public void testInitWithName() {
        ResourceName theResourceName = new ResourceName("test");
        assertEquals("test", theResourceName.get());
    }

    @Test
    public void testEquals() throws Exception {
        ResourceName theResourceName = new ResourceName("test");
        ResourceName theResourceName2 = new ResourceName("test");
        ResourceName theResourceName3 = new ResourceName("other");
        ResourceName theResourceName4 = new ResourceName(null);
        assertTrue(theResourceName.equals(theResourceName));
        assertTrue(theResourceName.equals(theResourceName2));
        assertFalse(theResourceName.equals(theResourceName3));
        assertFalse(theResourceName.equals(null));
        assertFalse(theResourceName.equals("test"));
        assertFalse(theResourceName.equals(theResourceName4));
        assertFalse(theResourceName4.equals(theResourceName));
    }

    @Test
    public void testHashCode() throws Exception {
        assertEquals(0, new ResourceName(null).hashCode());
        assertEquals("test".hashCode(), new ResourceName("test").hashCode());
    }

    @Test
    public void testSerialize() throws Exception {
        ResourceName theResourceName1 = new ResourceName(null);
        Map<String, Object> theData = theResourceName1.serialize();
        assertEquals(1, theData.size());
        assertEquals(null, theData.get("name"));

        ResourceName theResourceName2 = new ResourceName("test");
        Map<String, Object> theData2 = theResourceName2.serialize();
        assertEquals(1, theData2.size());
        assertEquals("test", theData2.get("name"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("name", "test");
        ResourceName theName = ResourceName.deserialize(theData);
        assertEquals("test", theName.get());
    }
}