package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class Game {

    public static final String NAME_PROPERTY = "name";
    public static final String DEFAULT_SCENE_PROPERTY = "defaultScene";
    public static final String ENABLE_WEB_GL_PROPERTY = "enableWebGL";

    private final Property<String> name;
    private final Property<String> defaultScene;
    private final Property<Boolean> enableWebGL;

    public Game() {
        name = new Property<>(String.class, this, NAME_PROPERTY, (String) null);
        defaultScene = new Property<>(String.class, this, DEFAULT_SCENE_PROPERTY, (String) null);
        enableWebGL = new Property<>(Boolean.class, this, ENABLE_WEB_GL_PROPERTY, Boolean.TRUE);
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<String> defaultSceneProperty() {
        return defaultScene;
    }

    public Property<Boolean> enableWebGLProperty() {
        return enableWebGL;
    }

    public void removeScene(String aSceneName) {
        if (aSceneName.equals(defaultScene.get())) {
            defaultScene.set(null);
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put("defaultscene", defaultScene.get());
        theResult.put("enablewebgl", Boolean.toString(enableWebGL.get()));
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.name.setQuietly((String) aSerializedData.get(NAME_PROPERTY));
        theResult.defaultScene.setQuietly((String) aSerializedData.get("defaultscene"));

        String theWebGl = (String) aSerializedData.get("enablewebgl");
        if (theWebGl != null) {
            theResult.enableWebGL.setQuietly(Boolean.valueOf(theWebGl));
        }
        return theResult;
    }
}