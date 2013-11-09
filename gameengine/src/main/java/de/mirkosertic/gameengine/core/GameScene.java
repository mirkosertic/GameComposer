package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Color;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.util.*;

public class GameScene {

    private String name;
    private Set<GameObject> objects;
    private Set<GameObjectInstance> instances;
    private Set<EventSheet> eventSheets;
    private GameRuntime gameRuntime;
    private GameObject cameraObject;
    private GameObject defaultPlayer;
    private Color backgroundColor;

    public GameScene(GameRuntime aGameRuntime) {
        backgroundColor = new Color(0, 0, 0);
        instances = new HashSet<GameObjectInstance>();
        objects = new HashSet<GameObject>();
        eventSheets = new HashSet<EventSheet>();
        gameRuntime = aGameRuntime;
    }

    public GameRuntime getRuntime() {
        return gameRuntime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameObject getCameraObject() {
        return cameraObject;
    }

    public void setCameraObject(GameObject cameraObject) {
        this.cameraObject = cameraObject;
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(Color backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public GameObject getDefaultPlayer() {
        return defaultPlayer;
    }

    public void setDefaultPlayer(GameObject defaultPlayer) {
        this.defaultPlayer = defaultPlayer;
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
        theResult.put("name", name);

        List<Map<String, Object>> theObjects = new ArrayList<Map<String, Object>>();
        for (GameObject theObject : objects) {
            theObjects.add(theObject.serialize());
        }
        theResult.put("objects", theObjects);
        if (cameraObject != null) {
            theResult.put("cameraobjectid", cameraObject.uuidProperty().get());
        }
        if (backgroundColor != null) {
            theResult.put("backgroundcolor", backgroundColor.serialize());
        }
        if (defaultPlayer != null) {
            theResult.put("defaultplayerobjectid", defaultPlayer.uuidProperty().get());
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
        theScene.name = (String) aSerializedData.get("name");

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
            theScene.cameraObject = theScene.findGameObjectByID(theCameraObject);
        }
        String theDefaultPlayerObject = (String) aSerializedData.get("defaultplayerobjectid");
        if (theDefaultPlayerObject != null) {
            theScene.defaultPlayer = theScene.findGameObjectByID(theDefaultPlayerObject);
        }
        Map<String, Object> theBackgroundColor = (Map<String, Object>) aSerializedData.get("backgroundcolor");
        if (theBackgroundColor != null) {
            theScene.backgroundColor = Color.deserialize(theBackgroundColor);
        }
        return theScene;
    }

    public void removeGameObjectInstance(GameObjectInstance aInstance) {
        instances.remove(aInstance);
        gameRuntime.getEventManager().fire(new GameObjectInstanceRemovedFromSceneEvent(aInstance));
    }

    public void updateObjectInstancePosition(GameObjectInstance aInstance, Position aNewPosition) {
        aInstance.positionProperty().set(aNewPosition);
        gameRuntime.getEventManager().fire(new GameObjectInstancePositionChangedEvent(aInstance, aNewPosition));
    }

    public void updateObjectInstanceAngle(GameObjectInstance aInstance, Angle aNewAngle) {
        aInstance.rotationAngleProperty().set(aNewAngle);
        gameRuntime.getEventManager().fire(new GameObjectInstanceAngleChangedEvent(aInstance, aNewAngle));
    }

    public void updateObjectSize(GameObject aObject, Size aNewSize) {
        aObject.sizeProperty().set(aNewSize);
        for (GameObjectInstance theInstance : instances) {
            if (theInstance.getOwnerGameObject() == aObject) {
                gameRuntime.getEventManager().fire(new GameObjectInstanceSizeChangedEvent(theInstance, aNewSize));
            }
        }
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

    public void updateObjectConfiguration(GameObject aGameObject) {
        gameRuntime.getEventManager().fire(new GameObjectConfigurationChangedEvent(aGameObject));
    }
}