package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Position;

public class GameScene {

    private Property<String> name;
    private Set<GameObject> objects;
    private Set<GameObjectInstance> instances;
    private Property<GameObject> cameraObject;
    private Property<GameObject> defaultPlayer;
    private Property<Color> backgroundColor;

    private Set<EventSheet> eventSheets;
    private GameRuntime gameRuntime;

    public GameScene(GameRuntime aGameRuntime) {

        GameEventManager theManager = aGameRuntime.getEventManager();

        name = new Property<String>(this, "name", null, theManager);
        cameraObject = new Property<GameObject>(this, "cameraObject", null, theManager);
        defaultPlayer = new Property<GameObject>(this, "defaultPlayer", null, theManager);
        backgroundColor = new Property<Color>(this, "color", new Color(0, 0, 0), theManager);
        instances = new HashSet<GameObjectInstance>();
        objects = new HashSet<GameObject>();
        eventSheets = new HashSet<EventSheet>();
        gameRuntime = aGameRuntime;
    }

    public GameRuntime getRuntime() {
        return gameRuntime;
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<GameObject> cameraObjectProperty() {
        return cameraObject;
    }

    public Property<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    public Property<GameObject> defaultPlayerProperty() {
        return defaultPlayer;
    }

    public void addGameObject(GameObject aObject) {
        objects.add(aObject);
        gameRuntime.getEventManager().fire(new GameObjectAddedToSceneEvent(aObject));
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

    public Set<EventSheet> getEventSheets() {
        return Collections.unmodifiableSet(eventSheets);
    }

    public List<GameObjectInstance> findAllAt(Position aPosition) {
        List<GameObjectInstance> theResult = new ArrayList<GameObjectInstance>();
        for (GameObjectInstance theInstance : instances) {
            if (theInstance.contains(aPosition)) {
                theResult.add(theInstance);
            }
        }
        return theResult;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name.get());

        List<Map<String, Object>> theObjects = new ArrayList<Map<String, Object>>();
        for (GameObject theObject : objects) {
            theObjects.add(theObject.serialize());
        }
        theResult.put("objects", theObjects);
        if (cameraObject.get() != null) {
            theResult.put("cameraobjectid", cameraObject.get().uuidProperty().get());
        }
        if (backgroundColor.get() != null) {
            theResult.put("backgroundcolor", backgroundColor.get().serialize());
        }
        if (defaultPlayer.get() != null) {
            theResult.put("defaultplayerobjectid", defaultPlayer.get().uuidProperty().get());
        }

        List<Map<String, Object>> theInstances = new ArrayList<Map<String, Object>>();
        for (GameObjectInstance theInstance : instances) {
            theInstances.add(theInstance.serialize());
        }
        theResult.put("instances", theInstances);
        return theResult;
    }

    public GameObject findGameObjectByID(String aObjectID) {
        for (GameObject theObject : objects) {
            if (theObject.uuidProperty().get().equals(aObjectID)) {
                return theObject;
            }
        }
        return null;
    }

    public static GameScene deserialize(GameRuntime aGameRuntime, Map<String, Object> aSerializedData) {
        GameScene theScene = new GameScene(aGameRuntime);
        theScene.name.setQuietly((String) aSerializedData.get("name"));

        List<Map<String, Object>> theObjects = (List<Map<String, Object>>) aSerializedData.get("objects");
        for (Map<String, Object> theObject : theObjects) {
            theScene.addGameObject(GameObject.deserialize(aGameRuntime, theScene, theObject));
        }

        List<Map<String, Object>> theInstances = (List<Map<String, Object>>) aSerializedData.get("instances");
        for (Map<String, Object> theInstance : theInstances) {
            theScene.addGameObjectInstance(GameObjectInstance.deserialize(aGameRuntime, theScene, theInstance));
        }

        String theCameraObject = (String) aSerializedData.get("cameraobjectid");
        if (theCameraObject != null) {
            theScene.cameraObject.setQuietly(theScene.findGameObjectByID(theCameraObject));
        }
        String theDefaultPlayerObject = (String) aSerializedData.get("defaultplayerobjectid");
        if (theDefaultPlayerObject != null) {
            theScene.defaultPlayer.setQuietly(theScene.findGameObjectByID(theDefaultPlayerObject));
        }
        Map<String, Object> theBackgroundColor = (Map<String, Object>) aSerializedData.get("backgroundcolor");
        if (theBackgroundColor != null) {
            theScene.backgroundColor.setQuietly(Color.deserialize(theBackgroundColor));
        }
        return theScene;
    }

    public void removeGameObjectInstance(GameObjectInstance aInstance) {
        instances.remove(aInstance);
        gameRuntime.getEventManager().fire(new GameObjectInstanceRemovedFromSceneEvent(aInstance));
    }

    public void removeGameObject(GameObject aGameObject) {
        Set<GameObjectInstance> theInstances = new HashSet<GameObjectInstance>(instances);
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aGameObject) {
                removeGameObjectInstance(theInstance);
            }
        }
        objects.remove(aGameObject);
        gameRuntime.getEventManager().fire(new GameObjectRemovedFromSceneEvent(aGameObject));
    }
}