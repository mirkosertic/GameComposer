package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;

import java.util.*;

public class Game {

    private final Property<String> name;
    private final Set<String> scenes;
    private final Property<String> defaultScene;

    public Game() {
        name = new Property<String>(this, "name", (String) null);
        defaultScene = new Property<String>(this, "defaultScene", (String) null);
        scenes = new HashSet<String>();
    }

    public Property<String> nameProperty() {
        return name;
    }

    public void addScene(String aSceneName) {
        scenes.add(aSceneName);
    }

    public Set<String> getScenes() {
        return Collections.unmodifiableSet(scenes);
    }

    public Property<String> defaultSceneProperty() {
        return defaultScene;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name.get());
        theResult.put("scenes", new ArrayList<String>(scenes));
        theResult.put("defaultscene", defaultScene.get());
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.name.setQuietly((String) aSerializedData.get("name"));
        theResult.defaultScene.setQuietly((String) aSerializedData.get("defaultscene"));

        Collection<String> theScenes = (Collection<String>) aSerializedData.get("scenes");
        for (String theScene : theScenes) {
            theResult.addScene(theScene);
        }

        return theResult;
    }
}
