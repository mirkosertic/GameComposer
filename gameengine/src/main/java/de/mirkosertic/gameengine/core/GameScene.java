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

    private final Property<String> name;
    private final Property<GameObject> cameraObject;
    private final Property<GameObject> defaultPlayer;
    private final Property<Color> backgroundColor;
    private final List<GameObject> objects;
    private final List<GameObjectInstance> instances;
    private final List<EventSheet> eventSheets;

    private final GameRuntime gameRuntime;

    public GameScene(GameRuntime aGameRuntime) {

        GameEventManager theManager = aGameRuntime.getEventManager();

        name = new Property<String>(this, "name", null, theManager);
        cameraObject = new Property<GameObject>(this, "cameraObject", null, theManager);
        defaultPlayer = new Property<GameObject>(this, "defaultPlayer", null, theManager);
        backgroundColor = new Property<Color>(this, "color", new Color(0, 0, 0), theManager);
        instances = new ArrayList<GameObjectInstance>();
        objects = new ArrayList<GameObject>();
        eventSheets = new ArrayList<EventSheet>();
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

    public GameObject createNewGameObject(String aName) {
        GameObject theObject = new GameObject(this, aName);
        addGameObject(theObject);
        return theObject;
    }

    private void addGameObject(GameObject aObject) {
        objects.add(aObject);
        gameRuntime.getEventManager().fire(new GameObjectAddedToSceneEvent(this, aObject));
    }

    public void addGameObjectInstance(GameObjectInstance aInstance) {
        instances.add(aInstance);
        gameRuntime.getEventManager().fire(new GameObjectInstanceAddedToSceneEvent(this, aInstance));
    }

    public List<GameObjectInstance> getInstances() {
        return Collections.unmodifiableList(instances);
    }

    public List<GameObject> getObjects() {
        return Collections.unmodifiableList(objects);
    }

    public List<EventSheet> getEventSheets() {
        return Collections.unmodifiableList(eventSheets);
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

        List<Map<String, Object>> theEventSheets = new ArrayList<Map<String, Object>>();
        for (EventSheet theSheet : eventSheets) {
            theEventSheets.add(theSheet.serialize());
        }
        theResult.put("eventsheets", theEventSheets);
        return theResult;
    }

    public GameObject findGameObjectByID(String aObjectUUID) {
        for (GameObject theObject : objects) {
            if (theObject.uuidProperty().get().equals(aObjectUUID)) {
                return theObject;
            }
        }
        return null;
    }

    public GameObjectInstance findGameObjectInstanceByID(String aInstanceUUID) {
        for (GameObjectInstance theInstance : instances) {
            if (theInstance.uuidProperty().get().equals(aInstanceUUID)) {
                return theInstance;
            }
        }
        return null;
    }

    public GameObjectInstance createFrom(GameObject aGameObject) {
        GameObjectInstance theInstance = new GameObjectInstance(gameRuntime.getEventManager(), aGameObject);
        theInstance.nameProperty().setQuietly(aGameObject.nameProperty().get() + "_" + System.currentTimeMillis());
        for (GameComponentTemplate theFactory : aGameObject.getComponentTemplates()) {
            theInstance.addComponent(theFactory.create(theInstance, gameRuntime));
        }
        return theInstance;
    }

    public static GameScene deserialize(GameRuntime aGameRuntime, Map<String, Object> aSerializedData) {
        GameScene theScene = new GameScene(aGameRuntime);
        theScene.name.setQuietly((String) aSerializedData.get("name"));

        List<Map<String, Object>> theObjects = (List<Map<String, Object>>) aSerializedData.get("objects");
        for (Map<String, Object> theObject : theObjects) {
            theScene.addGameObject(GameObject.deserialize(aGameRuntime, theScene, theObject));
        }

        List<Map<String, Object>> theInstances = (List<Map<String, Object>>) aSerializedData.get("instances");
        if (theInstances != null) {
            for (Map<String, Object> theInstance : theInstances) {
                theScene.addGameObjectInstance(GameObjectInstance.deserialize(aGameRuntime, theScene, theInstance));
            }
        }

        List<Map<String, Object>> theEventSheets = (List<Map<String, Object>>) aSerializedData.get("eventsheets");
        if (theEventSheets != null) {
            for (Map<String, Object> theSheet : theEventSheets) {
                theScene.eventSheets.add(EventSheet.unmarshall(aGameRuntime.getIORegistry(), theScene, theSheet));
            }
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
        gameRuntime.getEventManager().fire(new GameObjectInstanceRemovedFromSceneEvent(this, aInstance));
    }

    public void removeGameObject(GameObject aGameObject) {
        Set<GameObjectInstance> theInstances = new HashSet<GameObjectInstance>(instances);
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aGameObject) {
                removeGameObjectInstance(theInstance);
            }
        }
        objects.remove(aGameObject);
        gameRuntime.getEventManager().fire(new GameObjectRemovedFromSceneEvent(this, aGameObject));
    }

    public EventSheet createNewEventSheet() {
        EventSheet theSheet = new EventSheet(this);
        eventSheets.add(theSheet);
        gameRuntime.getEventManager().fire(new EventSheetAddedToSceneEvent(theSheet));
        return theSheet;
    }
}