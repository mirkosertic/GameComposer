package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public class TouchPositionTest {

    @Test
    public void testInit() {
        TouchIdentifier theIdentifier = new TouchIdentifier(13);
        TouchPosition thePosition = new TouchPosition(theIdentifier, 10, 12);
        assertSame(thePosition.identifier, theIdentifier);
        assertEquals(10, thePosition.x);
        assertEquals(12, thePosition.y);
    }

}