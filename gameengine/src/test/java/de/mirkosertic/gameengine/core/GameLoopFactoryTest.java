package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GameLoopFactoryTest {

    @Test
    public void testCreate() throws Exception {

        GameEventManager theEventManager = new GameEventManager(mock(Logger.class));

        GameScene theScene = mock(GameScene.class);
        GameView theView = mock(GameView.class);

        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        GameLoopFactory theFactory = new GameLoopFactory();
        GameLoop theLoop = theFactory.create(theScene, theView, theRuntime);
        assertSame(theScene, theLoop.getScene());
        assertSame(theView, theLoop.getHumanGameView());
        assertFalse(theLoop.isShutdown());
        theEventManager.fire(new SceneShutdown());
        assertTrue(theLoop.isShutdown());
    }
}