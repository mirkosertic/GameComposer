package de.mirkosertic.gameengine.core;

import java.util.*;

public class Game {

    private String name;
    private Set<String> scenes;
    private String defaultScene;

    public Game() {
        scenes = new HashSet<String>();
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

    public String getDefaultScene() {
        return defaultScene;
    }

    public void setDefaultScene(String defaultScene) {
        this.defaultScene = defaultScene;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name);
        theResult.put("scenes", new ArrayList<String>(scenes));
        theResult.put("defaultscene", defaultScene);
        return theResult;
    }

    public static Game deserialize(Map<String, Object> aSerializedData) {
        Game theResult = new Game();
        theResult.setName((String) aSerializedData.get("name"));
        theResult.setDefaultScene((String) aSerializedData.get("defaultscene"));

        Collection<String> theScenes = (Collection<String>) aSerializedData.get("scenes");
        for (String theScene : theScenes) {
            theResult.addScene(theScene);
        }

        return theResult;
    }
}
