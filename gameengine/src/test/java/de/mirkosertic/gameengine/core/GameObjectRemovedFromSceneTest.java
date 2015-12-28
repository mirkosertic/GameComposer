package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

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