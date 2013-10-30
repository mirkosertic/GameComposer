package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameScene {

    private String name;
    private Set<GameObject> objects;
    private Set<GameObjectInstance> instances;
    private GameRuntime gameRuntime;

    public GameScene(GameRuntime aGameRuntime) {
        instances = new HashSet<>();
        objects = new HashSet<>();
        gameRuntime = aGameRuntime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addGameObject(GameObject aObject) {
        objects.add(aObject);
    }

    public void addGameObjectInstance(GameObjectInstance aInstance) {
        instances.add(aInstance);
        gameRuntime.getEventManager().fire(new GameObjectInstanceAddedToSceneEvent(aInstance));
    }

    public Set<GameObjectInstance> getInstances() {
        return Collections.unmodifiableSet(instances);
    }

    public Set<GameObject> getObjects() {
        return Collections.unmodifiableSet(objects);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("name", name);

        List<Map<String, Object>> theObjects = new ArrayList<>();
        for (GameObject theObject : objects) {
            theObjects.add(theObject.serialize());
        }
        theResult.put("objects", theObjects);

        List<Map<String, Object>> theInstances = new ArrayList<>();
        for (GameObjectInstance theInstance : instances) {
            theInstances.add(theInstance.serialize());
        }
        theResult.put("instances", theInstances);
        return theResult;
    }

    public static GameScene deserialize(GameRuntime aGameRuntime, Map<String, Object> aSerializedData) {
        GameScene theScene = new GameScene(aGameRuntime);
        theScene.name = (String) aSerializedData.get("name");

        List<Map<String, Object>> theObjects = (List<Map<String, Object>>) aSerializedData.get("objects");
        for (Map<String, Object> theObject : theObjects) {
            theScene.addGameObject(GameObject.deserialize(aGameRuntime, theObject));
        }

        List<Map<String, Object>> theInstances = (List<Map<String, Object>>) aSerializedData.get("instances");
        for (Map<String, Object> theInstance : theInstances) {
            theScene.addGameObjectInstance(GameObjectInstance.deserialize(aGameRuntime, theInstance));
        }
        return theScene;
    }
}
