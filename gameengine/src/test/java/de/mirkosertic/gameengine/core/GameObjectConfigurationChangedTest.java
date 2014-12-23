package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameObjectConfigurationChangedTest {

    @Test
    public void testInit() {
        GameObject theObject = mock(GameObject.class);
        GameObjectConfigurationChanged theEvent = new GameObjectConfigurationChanged(theObject);
        assertSame(theObject, theEvent.object);
        assertEquals("GameObjectConfigurationChanged", theEvent.type);
    }

}