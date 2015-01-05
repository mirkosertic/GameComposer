package de.mirkosertic.gameengine.scriptengine.lua;

import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Script;

import org.junit.Test;

import static org.junit.Assert.*;

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
}