package de.mirkosertic.gameengine.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GameTest {

    @Test
    public void testNameProperty() throws Exception {
        Game theGame = new Game();
        assertEquals(Game.NAME_PROPERTY, theGame.nameProperty().getName());
        assertEquals(String.class, theGame.nameProperty().getType());
        assertNull(theGame.nameProperty().get());
        assertSame(theGame, theGame.nameProperty().getOwner());
    }

    @Test
    public void testDefaultSceneProperty() throws Exception {
        Game theGame = new Game();
        assertEquals(Game.DEFAULT_SCENE_PROPERTY, theGame.defaultSceneProperty().getName());
        assertEquals(String.class, theGame.defaultSceneProperty().getType());
        assertNull(theGame.defaultSceneProperty().get());
        assertSame(theGame, theGame.defaultSceneProperty().getOwner());
    }

    @Test
    public void testEnableWebGLProperty() throws Exception {
        Game theGame = new Game();
        assertEquals(Game.ENABLE_WEB_GL_PROPERTY, theGame.enableWebGLProperty().getName());
        assertEquals(Boolean.class, theGame.enableWebGLProperty().getType());
        assertTrue(theGame.enableWebGLProperty().get());
        assertSame(theGame, theGame.enableWebGLProperty().getOwner());
    }

    @Test
    public void testRemoveScene() throws Exception {
        Game theGame = new Game();
        theGame.defaultSceneProperty().set("test");
        theGame.removeScene("scene1");
        assertEquals("test", theGame.defaultSceneProperty().get());
        theGame.removeScene("test");
        assertNull(theGame.defaultSceneProperty().get());
    }

    @Test
    public void testSerialize() throws Exception {
        Game theGame = new Game();
        theGame.nameProperty().set("Testgame");
        theGame.defaultSceneProperty().set("scene1");
        theGame.customPropertiesProperty().get().set("key", "value");
        Map<String, Object> theData = theGame.serialize();
        assertEquals(8, theData.size());
        assertEquals("Testgame", theData.get(Game.NAME_PROPERTY));
        assertEquals("scene1", theData.get("defaultscene"));
        assertEquals("true", theData.get("enablewebgl"));
        assertEquals("false", theData.get("enableDebug"));
        assertEquals("false", theData.get("enableNetworking"));
        assertEquals(0, ((List<String>)theData.get("scenes")).size());
        assertEquals("https://glowing-heat-2189.firebaseio.com", theData.get("firebaseURL"));

        Map<String, String> theProps = (Map<String, String>) theData.get("customProperties");
        assertEquals(1, theProps.size(), 0);
    }

    @Test
    public void testDeserialize1() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put(Game.NAME_PROPERTY, "Testgame");
        theData.put("defaultscene", "scene1");
        theData.put("enableNetworking", "true");
        theData.put("firebaseURL", "lala");
        Game theGame = Game.deserialize(theData);
        assertEquals("Testgame", theGame.nameProperty().get());
        assertEquals("scene1", theGame.defaultSceneProperty().get());
        assertTrue(theGame.enableWebGLProperty().get());
        assertTrue(theGame.enableNetworkingProperty().get());
        assertEquals("lala", theGame.fireBaseURLProperty().get());
    }

    @Test
    public void testDeserialize2() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put(Game.NAME_PROPERTY, "Testgame");
        theData.put("defaultscene", "scene1");
        theData.put("enablewebgl", "false");
        theData.put("enableDebug", "true");

        Game theGame = Game.deserialize(theData);
        assertEquals("Testgame", theGame.nameProperty().get());
        assertEquals("scene1", theGame.defaultSceneProperty().get());
        assertFalse(theGame.enableWebGLProperty().get());
        assertTrue(theGame.enableDebugProperty().get());
        assertFalse(theGame.enableNetworkingProperty().get());
        assertEquals("https://glowing-heat-2189.firebaseio.com", theGame.fireBaseURLProperty().get());
    }

    @Test
    public void testRoundtripJSON() throws IOException {
        ObjectMapper theMapper = new ObjectMapper();

        Game theGame = new Game();
        String theJSON = theMapper.writeValueAsString(theGame.serialize());
        Game theOtherGame = Game.deserialize(theMapper.readValue(theJSON, Map.class));
    }
}