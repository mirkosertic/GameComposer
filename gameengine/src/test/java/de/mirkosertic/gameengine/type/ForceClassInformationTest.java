package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ForceClassInformationTest {

    @Test
    public void testForceX() {
        Force theForce = new Force(10, 20);
        ForceClassInformation theInformation = ForceClassInformation.INSTANCE;
        Field theField = theInformation.getFieldByName("forceX");
        assertEquals(2, theInformation.getFields().length);
        assertSame(theField, ForceClassInformation.FORCEX);
        assertEquals("forceX", theField.getName());
        assertEquals(Float.class, theField.getType());
        assertEquals(10f, theField.getValue(theForce));
    }

    @Test
    public void testForceY() {
        Force theForce = new Force(10, 20);
        ForceClassInformation theInformation = ForceClassInformation.INSTANCE;
        Field theField = theInformation.getFieldByName("forceY");
        assertEquals(2, theInformation.getFields().length);
        assertSame(theField, ForceClassInformation.FORCEY);
        assertEquals("forceY", theField.getName());
        assertEquals(Float.class, theField.getType());
        assertEquals(20f, theField.getValue(theForce));
    }
}