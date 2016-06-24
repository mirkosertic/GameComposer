package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.SystemException;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameLoopTest {

    @Test
    public void testGetHumanGameView() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        assertSame(theGameView, theLoop.getHumanGameView());
    }

    @Test
    public void testGetScene() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        assertSame(theScene, theLoop.getScene());
    }

    @Test
    public void testRun() throws Exception {

    }

    @Test
    public void testSingleRunOnlyUpdateGameViewFirstTime() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRunOnlyUpdateGameView();
        assertEquals(0, theLoop.getStatistics().getNumberTicks());
    }

    @Test
    public void testSingleRunOnlyUpdateGameView() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRunOnlyUpdateGameView();
        Thread.sleep(10);
        theLoop.singleRunOnlyUpdateGameView();

        verify(theGameView).renderGame(anyLong(), anyLong(), eq(theScene), any(RuntimeStatistics.class));
        assertEquals(1, theLoop.getStatistics().getNumberTicks());
    }

    @Test
    public void testSingleRunOnlyUpdateGameViewError() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        doThrow(new RuntimeException()).when(theGameView).renderGame(anyLong(), anyLong(), eq(theScene), any(RuntimeStatistics.class));
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRunOnlyUpdateGameView();
        Thread.sleep(10);
        theLoop.singleRunOnlyUpdateGameView();

        verify(theManager).fire(any(SystemException.class));
    }

    @Test
    public void testSingleRunFirstTime() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theEventManager = mock(GameEventManager.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRun();
        assertEquals(0, theLoop.getStatistics().getNumberTicks());
        verify(theEventManager).fire(any(SceneStarted.class));
    }

    @Test
    public void testSingleRun() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theEventManager = mock(GameEventManager.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        GameSystemWork theWork = mock(GameSystemWork.class);

        GameSystem theSystem = mock(GameSystem.class);
        when(theSystem.proceedGame(anyLong(), anyLong(), anyLong())).thenReturn(theWork);
        when(theRuntime.getSystems()).thenReturn(new GameSystem[] {theSystem});

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRun();
        Thread.sleep(10);
        theLoop.singleRun();

        verify(theSystem).proceedGame(anyLong(), anyLong(), anyLong());
        verify(theGameView).renderGame(anyLong(), anyLong(), eq(theScene), any(RuntimeStatistics.class));
        verify(theWork, times(1)).runInFrame();
        verify(theWork, times(1)).runAfterFrame();
        assertEquals(1, theLoop.getStatistics().getNumberTicks());
    }

    @Test
    public void testSingleRunError() throws Exception {

        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);
        GameEventManager theManager = mock(GameEventManager.class);
        when(theRuntime.getEventManager()).thenReturn(theManager);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        theLoop.singleRun();
        Thread.sleep(10);
        theLoop.singleRun();

        verify(theManager, times(2)).fire(any(GameEvent.class));
    }

    @Test
    public void testShutdown() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameView theGameView = mock(GameView.class);
        GameRuntime theRuntime = mock(GameRuntime.class);

        GameLoop theLoop = new GameLoop(theScene, theGameView, theRuntime);
        assertFalse(theLoop.isShutdown());
        theLoop.shutdown();
        assertTrue(theLoop.isShutdown());
    }
}