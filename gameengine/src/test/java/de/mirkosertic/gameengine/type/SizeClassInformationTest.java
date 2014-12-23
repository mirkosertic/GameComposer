package de.mirkosertic.gameengine.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class SizeClassInformationTest {

    @Test
    public void testWidth() {
        Size theSize = new Size(10, 20);
        SizeClassInformation theInformation = SizeClassInformation.INSTANCE;
        Field theField = theInformation.getFieldByName("width");
        assertEquals(2, theInformation.getFields().length);
        assertNotNull(theInformation.getFieldByName("height"));
        assertSame(SizeClassInformation.WIDTH, theField);
        assertEquals("width", theField.getName());
        assertEquals(Integer.class, theField.getType());
        assertEquals(10, theField.getValue(theSize));
    }

    @Test
    public void testHeight() {
        Size theSize = new Size(10, 20);
        SizeClassInformation theInformation = SizeClassInformation.INSTANCE;
        Field theField = theInformation.getFieldByName("height");
        assertEquals(2, theInformation.getFields().length);
        assertNotNull(theInformation.getFieldByName("width"));
        assertSame(SizeClassInformation.HEIGHT, theField);
        assertEquals("height", theField.getName());
        assertEquals(Integer.class, theField.getType());
        assertEquals(20, theField.getValue(theSize));
    }

}