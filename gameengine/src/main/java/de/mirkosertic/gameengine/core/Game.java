package de.mirkosertic.gameengine.core;

import java.util.*;

public class Game {

    private String name;
    private Set<String> scenes;

    public Game() {
        scenes = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addScene(String aSceneName) {
        scenes.add(aSceneName);
    }

    public Set<String> getScenes() {
        return Collections.unmodifiableSet(scenes);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("name", name);
        theResult.put("scenes", new ArrayList<>(scenes));
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.setName((String) aSerializedData.get("name"));

        Collection<String> theScenes = (Collection<String>) aSerializedData.get("scenes");
        for (String theScene : theScenes) {
            theResult.addScene(theScene);
        }

        return theResult;
    }
}
