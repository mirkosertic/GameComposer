package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class LuaScriptEngineTest {


    @Test
    public void testShutdown() throws Exception {
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(""));
        theEngine.shutdown();
    }

    @Test
    public void testProceedGame1() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return aGameTime + aElapsedTimeSinceLastLoop + counter end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerPrimitive("counter", 17);
        assertEquals(21L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGame2() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return color.r() end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("color", new Color(10, 20, 30));
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGame3() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return position.changeX(10).x() end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("position", new Position());
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testProceedGameWithInvalidFieldWrite() throws Exception {
        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return position.x(10) end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("position", new Position());
        assertEquals(10L, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGameWithPropertyRead() throws IOException {

        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        Game theGame = mock(Game.class);

        GameScene theGameScene = new GameScene(theGame, theRuntime);
        GameObject theObject = new GameObject(theGameScene, "Test");

        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) return go.nameProperty() end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("go", theObject);
        assertEquals("Test", theEngine.proceedGame(1, 3));
    }

    @Test
    public void testProceedGameWithPropertyWrite() throws IOException {

        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);

        Game theGame = mock(Game.class);
        GameScene theGameScene = new GameScene(theGame, theRuntime);
        GameObject theObject = new GameObject(theGameScene, "Test");

        String theScript = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) go.nameProperty('hallo'); return go end";
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(new Script(theScript));
        theEngine.registerObject("go", theObject);
        theEngine.proceedGame(1, 3);
        assertEquals("hallo", theObject.nameProperty().get());
        assertSame(theObject, theEngine.proceedGame(1, 3));
    }

    @Test
    public void testEvaluateSimpleExpression() throws IOException {

        GameEventManager theEventManager = new GameEventManager();
        GameRuntime theRuntime = mock(GameRuntime.class);
        when(theRuntime.getEventManager()).thenReturn(theEventManager);
        Game theGame = new Game();
        GameScene theGameScene = new GameScene(theGame, theRuntime);
        GameObject theObject = new GameObject(theGameScene, "Test");
        GameObjectInstance theInstance = theGameScene.createFrom(theObject);


        TextExpression theExpression = new TextExpression("\"hello\" .. \" world\"");
        LuaJScriptEngineFactory theFactory = new LuaJScriptEngineFactory(new TestBuildInFunctions());
        LuaJScriptEngine theEngine = theFactory.createNewEngine(theExpression);

        assertEquals("hello world", theEngine.evaluateSimpleExpressionFor(theInstance));
    }
}