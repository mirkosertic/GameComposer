package de.mirkosertic.gameengine.core;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Test;

public class GameObjectRemovedFromSceneTest {

    @Test
    public void testInit() {
        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        GameObjectRemovedFromScene theEvent = new GameObjectRemovedFromScene(theScene, theObject);
        assertSame(theScene, theEvent.scene);
        assertSame(theObject, theEvent.object);
        assertEquals("GameObjectRemovedFromScene", theEvent.type);
    }
}