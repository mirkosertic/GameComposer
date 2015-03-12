package de.mirkosertic.gameengine.event;

import org.junit.Test;

import static org.junit.Assert.*;

public class ReadOnlyPropertyTest {

    @Test
    public void testGetType() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", "default");
        assertSame(String.class, theProperty.getType());
    }

    @Test
    public void testGetOwner() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", "default");
        assertSame(this, theProperty.getOwner());
    }

    @Test
    public void testGetName() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", "default");
        assertEquals("property", theProperty.getName());
    }

    @Test
    public void testGet() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", "default");
        assertEquals("default", theProperty.get());
    }

    @Test
    public void testIsNullNegative() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", "default");
        assertFalse(theProperty.isNull());
    }

    @Test
    public void testIsNullPositive() throws Exception {
        ReadOnlyProperty<String> theProperty = new ReadOnlyProperty<>(String.class, this, "property", null);
        assertTrue(theProperty.isNull());
    }
}