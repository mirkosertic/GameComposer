package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.PositionAnchor;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameObjectInstanceTest {

    @Test
    public void testGetClassInformation() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertTrue(theInstance.getClassInformation() instanceof GameObjectInstanceClassInformation);
    }

    @Test
    public void testUuidProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertNotNull(theInstance.uuidProperty().get());
        assertEquals("uuid", theInstance.uuidProperty().getName());
        assertEquals(String.class, theInstance.uuidProperty().getType());
        theInstance.uuidProperty().set("test");
        assertEquals("test", theInstance.uuidProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testPositionProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("position", theInstance.positionProperty().getName());
        assertEquals(Position.class, theInstance.positionProperty().getType());
        theInstance.positionProperty().set(new Position(5, 6));
        assertEquals(new Position(5, 6), theInstance.positionProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testNameProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("name", theInstance.nameProperty().getName());
        assertEquals(String.class, theInstance.nameProperty().getType());
        theInstance.nameProperty().set("test");
        assertEquals("test", theInstance.nameProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testRotationAngleProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("rotationAngle", theInstance.rotationAngleProperty().getName());
        assertEquals(Angle.class, theInstance.rotationAngleProperty().getType());
        assertEquals(new Angle(0), theInstance.rotationAngleProperty().get());
        theInstance.rotationAngleProperty().set(new Angle(17));
        assertEquals(new Angle(17), theInstance.rotationAngleProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testVisibleProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("visible", theInstance.visibleProperty().getName());
        assertEquals(Boolean.class, theInstance.visibleProperty().getType());
        assertTrue(theInstance.visibleProperty().get());
        theInstance.visibleProperty().set(false);
        assertFalse(theInstance.visibleProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testAbsolutePositionAnchorProperty() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertEquals("positionAnchor", theInstance.positionAnchorProperty().getName());
        assertEquals(PositionAnchor.class, theInstance.positionAnchorProperty().getType());
        assertEquals(PositionAnchor.SCENE, theInstance.positionAnchorProperty().get());
        theInstance.positionAnchorProperty().set(PositionAnchor.BOTTOM_LEFT);
        assertEquals(PositionAnchor.BOTTOM_LEFT, theInstance.positionAnchorProperty().get());
        verify(theEventManager).handleGameEvent(any(PropertyChanged.class));
    }

    @Test
    public void testGetOwnerGameObject() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        Property theVisibleProperty = mock(Property.class);
        when(theVisibleProperty.get()).thenReturn(true);
        GameObject theOwner = mock(GameObject.class);
        when(theOwner.visibleProperty()).thenReturn(theVisibleProperty);

        GameObjectInstance theInstance = new GameObjectInstance(theEventManager, theOwner);
        assertSame(theOwner, theInstance.getOwnerGameObject());
    }
}
