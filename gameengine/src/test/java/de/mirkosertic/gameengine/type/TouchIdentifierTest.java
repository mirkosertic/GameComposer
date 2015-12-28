package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TouchIdentifierTest {

    @Test
    public void testEquals() throws Exception {
        TouchIdentifier theIdentifier = new TouchIdentifier(10);
        TouchIdentifier theIdentifier1 = new TouchIdentifier(10);
        TouchIdentifier theIdentifier2 = new TouchIdentifier(11);
        assertEquals(theIdentifier, theIdentifier);
        assertEquals(theIdentifier, theIdentifier1);
        assertNotEquals(theIdentifier, theIdentifier2);
        assertNotEquals(theIdentifier, "lala");
        assertNotEquals(theIdentifier, null);
    }

    @Test
    public void testHashCode() throws Exception {
        TouchIdentifier theIdentifier = new TouchIdentifier(10);
        assertEquals(10, theIdentifier.hashCode());
    }
}