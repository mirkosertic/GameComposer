package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameLoopFactoryTest {

    @Test
    public void testCreate() throws Exception {

        GameEventManager theEventManager = new GameEventManager();

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