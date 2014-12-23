package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Field;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameObjectClassInformationTest {

    @Test
    public void testUUID() {

        GameObjectClassInformation theInformation = GameObjectClassInformation.INSTANCE;

        Property theProperty = mock(Property.class);
        GameObject theObject = mock(GameObject.class);
        when(theObject.uuidProperty()).thenReturn(theProperty);

        Field theField = theInformation.getFieldByName("uuid");
        assertEquals("uuid", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theObject));

        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("size"));
        assertNotNull(theInformation.getFieldByName("visible"));
    }

    @Test
    public void testName() {

        GameObjectClassInformation theInformation = GameObjectClassInformation.INSTANCE;

        Property theProperty = mock(Property.class);
        GameObject theObject = mock(GameObject.class);
        when(theObject.nameProperty()).thenReturn(theProperty);

        Field theField = theInformation.getFieldByName("name");
        assertEquals("name", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theObject));

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("size"));
        assertNotNull(theInformation.getFieldByName("visible"));

    }

    @Test
    public void testSize() {

        GameObjectClassInformation theInformation = GameObjectClassInformation.INSTANCE;

        Property theProperty = mock(Property.class);
        GameObject theObject = mock(GameObject.class);
        when(theObject.sizeProperty()).thenReturn(theProperty);

        Field theField = theInformation.getFieldByName("size");
        assertEquals("size", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theObject));

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("visible"));

    }

    @Test
    public void testVisible() {
        GameObjectClassInformation theInformation = GameObjectClassInformation.INSTANCE;

        Property theProperty = mock(Property.class);
        GameObject theObject = mock(GameObject.class);
        when(theObject.visibleProperty()).thenReturn(theProperty);

        Field theField = theInformation.getFieldByName("visible");
        assertEquals("visible", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theObject));

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("size"));
    }
}