package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class PositionClassInformationTest {

    @Test
    public void testX() {
        Position thePosition = new Position(10f, 20f);
        PositionClassInformation theInformation = PositionClassInformation.INSTANCE;
        assertEquals(2, theInformation.getFields().length);

        Field theField = theInformation.getFieldByName("x");
        assertSame(theField, PositionClassInformation.X);
        assertEquals("x", theField.getName());
        assertEquals(Float.class, theField.getType());
        assertEquals(10f, theField.getValue(thePosition));
    }

    @Test
    public void testY() {
        Position thePosition = new Position(10f, 20f);
        PositionClassInformation theInformation = PositionClassInformation.INSTANCE;
        assertEquals(2, theInformation.getFields().length);

        Field theField = theInformation.getFieldByName("y");
        assertSame(theField, PositionClassInformation.Y);
        assertEquals("y", theField.getName());
        assertEquals(Float.class, theField.getType());
        assertEquals(20f, theField.getValue(thePosition));
    }
}