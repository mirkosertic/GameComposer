package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class ColorClassInformationTest {

    @Test
    public void testR() {

        Color theRGB = new Color(10, 20, 30);

        ColorClassInformation theInformation = ColorClassInformation.INSTANCE;
        assertEquals(3, theInformation.getFields().length);
        Field theField = theInformation.getFieldByName("r");
        assertSame(theField, ColorClassInformation.R);
        assertEquals("r", theField.getName());
        assertEquals(Integer.class, theField.getType());
        assertEquals(10, theField.getValue(theRGB));
    }

    @Test
    public void testG() {

        Color theRGB = new Color(10, 20, 30);

        ColorClassInformation theInformation = ColorClassInformation.INSTANCE;
        assertEquals(3, theInformation.getFields().length);
        Field theField = theInformation.getFieldByName("g");
        assertSame(theField, ColorClassInformation.G);
        assertEquals("g", theField.getName());
        assertEquals(Integer.class, theField.getType());
        assertEquals(20, theField.getValue(theRGB));
    }

    @Test
    public void testB() {

        Color theRGB = new Color(10, 20, 30);

        ColorClassInformation theInformation = ColorClassInformation.INSTANCE;
        assertEquals(3, theInformation.getFields().length);
        Field theField = theInformation.getFieldByName("b");
        assertSame(theField, ColorClassInformation.B);
        assertEquals("b", theField.getName());
        assertEquals(Integer.class, theField.getType());
        assertEquals(30, theField.getValue(theRGB));
    }
}