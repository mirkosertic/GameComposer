package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;

import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameRuntimeTest {

    @Test
    public void testGetResourceCache() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        ScriptEngineFactory theScriptEngineFactory = mock(ScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory);
        assertNotNull(theRuntime.getResourceCache());
    }

    @Test
    public void testGetEventManager() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        ScriptEngineFactory theScriptEngineFactory = mock(ScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory);
        assertSame(theEventManager, theRuntime.getEventManager());
    }

    @Test
    public void testAddSystem() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        ScriptEngineFactory theScriptEngineFactory = mock(ScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory);
        assertNotNull(theRuntime.getSystems());
        assertEquals(0, theRuntime.getSystems().length);
        GameSystem theSystem = mock(GameSystem.class);
        theRuntime.addSystem(theSystem);
        assertEquals(1, theRuntime.getSystems().length);
        assertSame(theSystem, theRuntime.getSystems()[0]);
    }

    @Test
    public void testGetIORegistry() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        ScriptEngineFactory theScriptEngineFactory = mock(ScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory);
        assertNotNull(theRuntime.getIORegistry());
    }

    @Test
    public void testGetScriptEngineFactory() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        ScriptEngineFactory theScriptEngineFactory = mock(ScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory);
        assertSame(theScriptEngineFactory, theRuntime.getScriptEngineFactory());
    }
}