package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.HashMap;
import java.util.Map;

public class Game {

    private final Property<String> name;
    private final Property<String> defaultScene;

    public Game() {
        name = new Property<String>(this, "name", (String) null);
        defaultScene = new Property<String>(this, "defaultScene", (String) null);
    }

    public Property<String> nameProperty() {
        return name;
    }

    public void removeScene(String aSceneName) {
        if (aSceneName.equals(defaultScene.get())) {
            defaultScene.set(null);
        }
    }

    public Property<String> defaultSceneProperty() {
        return defaultScene;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name.get());
        theResult.put("defaultscene", defaultScene.get());
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.name.setQuietly((String) aSerializedData.get("name"));
        theResult.defaultScene.setQuietly((String) aSerializedData.get("defaultscene"));
        return theResult;
    }
}