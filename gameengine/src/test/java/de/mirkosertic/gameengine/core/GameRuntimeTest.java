package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public class GameRuntimeTest {

    @Test
    public void testGetResourceCache() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        LUAScriptEngineFactory theScriptEngineFactory = mock(LUAScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory, new DefaultLogger());
        assertNotNull(theRuntime.getResourceCache());
    }

    @Test
    public void testGetEventManager() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        LUAScriptEngineFactory theScriptEngineFactory = mock(LUAScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory, new DefaultLogger());
        assertSame(theEventManager, theRuntime.getEventManager());
    }

    @Test
    public void testAddSystem() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        LUAScriptEngineFactory theScriptEngineFactory = mock(LUAScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory, new DefaultLogger());
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
        LUAScriptEngineFactory theScriptEngineFactory = mock(LUAScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory, new DefaultLogger());
        assertNotNull(theRuntime.getIORegistry());
    }

    @Test
    public void testGetScriptEngineFactory() throws Exception {
        GameEventManager theEventManager = mock(GameEventManager.class);
        GameResourceLoader theResourceLoader = mock(GameResourceLoader.class);
        LUAScriptEngineFactory theScriptEngineFactory = mock(LUAScriptEngineFactory.class);
        GameRuntime theRuntime = new GameRuntime(theEventManager, theResourceLoader, theScriptEngineFactory, new DefaultLogger());
        assertSame(theScriptEngineFactory, theRuntime.getScriptEngineFactory());
    }
}