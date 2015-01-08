package de.mirkosertic.gameengine.scriptengine.lua;

import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Script;

import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LuaScriptEngineTest {

    @Test
    public void testShutdown() throws Exception {
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(""));
        theEngine.shutdown();
    }

    @Test
    public void testProceedGame1() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return aGameTime + aElapsedTimeSinceLastLoop + counter end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerPrimitive("counter", 17);
        assertEquals(21L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGame2() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return color.r() end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("color", new Color(10, 20, 30));
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGame3() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return position.changeX(10).x() end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("position", new Position());
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProceedGameWithInvalidFieldWrite() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return position.x(10) end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("position", new Position());
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGameWithPropertyRead() throws IOException {

        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        GameScene theGameScene = new GameScene(theRuntime);
        GameObject theObject = new GameObject(theGameScene, "Test");

        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return go.nameProperty() end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("go", theObject);
        assertEquals("Test", theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGameWithPropertyWrite() throws IOException {

        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        GameScene theGameScene = new GameScene(theRuntime);
        GameObject theObject = new GameObject(theGameScene, "Test");

        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) go.nameProperty('hallo'); return go end";
        LuaScriptEngineFactory theFactory = new LuaScriptEngineFactory();
        LuaScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("go", theObject);
        theEngine.proceedGame(1, 3);
        assertEquals("hallo", theObject.nameProperty().get());
        assertSame(theObject, theEngine.proceedGame(1, 3));
    }
}