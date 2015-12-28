package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class GameObjectInstanceAddedToSceneTest {

    @Test
    public void testInit() {
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        GameObjectInstanceAddedToScene theEvent = new GameObjectInstanceAddedToScene(theInstance);
        assertSame(theInstance, theEvent.instance);
        assertEquals("GameObjectInstanceAddedToScene", theEvent.type);
    }

}