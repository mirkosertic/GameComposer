package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SizeClassInformationTest {

    @Test
    public void testWidth() {
        Size theSize = new Size(10, 20);
        SizeClassInformation theInformation = SizeClassInformation.INSTANCE;
        assertEquals("width", theInformation.getFieldByName("width").getName());
        assertEquals("height", theInformation.getFieldByName("height").getName());
        assertEquals(2, theInformation.getFields().length);

        Field theField = theInformation.getFieldByName("width");
        assertSame(SizeClassInformation.WIDTH, theField);
        assertEquals(Integer.class, theField.getType());
        assertEquals(10, theField.getValue(theSize));
    }

    @Test
    public void testHeight() {
        Size theSize = new Size(10, 20);
        SizeClassInformation theInformation = SizeClassInformation.INSTANCE;
        assertEquals("width", theInformation.getFieldByName("width").getName());
        assertEquals("height", theInformation.getFieldByName("height").getName());
        assertEquals(2, theInformation.getFields().length);

        Field theField = theInformation.getFieldByName("height");
        assertSame(SizeClassInformation.HEIGHT, theField);
        assertEquals(Integer.class, theField.getType());
        assertEquals(20, theField.getValue(theSize));
    }
}