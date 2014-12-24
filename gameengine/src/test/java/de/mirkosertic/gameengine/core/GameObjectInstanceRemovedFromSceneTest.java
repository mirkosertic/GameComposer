package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameObjectInstanceRemovedFromSceneTest {

    @Test
    public void testInit() {
        GameScene theScene = mock(GameScene.class);
        GameObjectInstance theInstance = mock(GameObjectInstance.class);
        GameObjectInstanceRemovedFromScene theEvent = new GameObjectInstanceRemovedFromScene(theScene, theInstance);
        assertEquals("GameObjectInstanceRemovedFromScene", theEvent.type);
        assertSame(theScene, theEvent.scene);
        assertSame(theInstance, theEvent.instance);
    }
}