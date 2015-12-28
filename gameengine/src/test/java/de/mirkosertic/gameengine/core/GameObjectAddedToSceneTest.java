package de.mirkosertic.gameengine.core;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class GameObjectAddedToSceneTest {

    @Test
    public void testInit() {
        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        GameObjectAddedToScene theEvent = new GameObjectAddedToScene(theScene, theObject);
        assertSame(theObject, theEvent.object);
        assertSame(theScene, theEvent.scene);
        assertEquals("GameObjectAddedToScene", theEvent.type);
    }

}