package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Field;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameObjectInstanceClassInformationTest {

    @Test
    public void testUUID() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.uuidProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("uuid");
        assertEquals("uuid", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testName() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.nameProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("name");
        assertEquals("name", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testPosition() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.positionProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("position");
        assertEquals("position", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testRotationAngle() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.rotationAngleProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("rotationAngle");
        assertEquals("rotationAngle", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testVisible() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.visibleProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("visible");
        assertEquals("visible", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testAbsolutePosition() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.absolutePositionProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("absolutePosition");
        assertEquals("absolutePosition", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }

    @Test
    public void testAbsolutePositionAnchor() {
        GameObjectInstanceClassInformation theInformation = GameObjectInstanceClassInformation.INSTANCE;

        assertNotNull(theInformation.getFieldByName("uuid"));
        assertNotNull(theInformation.getFieldByName("name"));
        assertNotNull(theInformation.getFieldByName("position"));
        assertNotNull(theInformation.getFieldByName("rotationAngle"));
        assertNotNull(theInformation.getFieldByName("visible"));
        assertNotNull(theInformation.getFieldByName("absolutePosition"));
        assertNotNull(theInformation.getFieldByName("absolutePositionAnchor"));

        Property theMock = mock(Property.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        when(theInstance.absolutePositionAnchorProperty()).thenReturn(theMock);

        Field theField = theInformation.getFieldByName("absolutePositionAnchor");
        assertEquals("absolutePositionAnchor", theField.getName());
        assertEquals(Property.class, theField.getType());
        assertEquals(theMock, theField.getValue(theInstance));
    }
}