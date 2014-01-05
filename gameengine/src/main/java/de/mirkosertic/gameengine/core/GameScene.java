package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Rectangle;
import de.mirkosertic.gameengine.type.TextExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameScene extends PropertyAware {

    public static final String NAME_PROPERTY = "name";
    public static final String CAMERA_OBJECT_PROPERTY = "cameraObject";
    public static final String DEFAULT_PLAYER_PROPERTY = "defaultPlayer";
    public static final String COLOR_PROPERTY = "color";
    public static final String LAYOUT_BOUNDS_PROPERTY = "layoutBounds";

    private final Property<String> name;
    private final Property<GameObject> cameraObject;
    private final Property<GameObject> defaultPlayer;
    private final Property<Color> backgroundColor;
    private final Property<Rectangle> layoutBounds;

    private GameObject[] objects;
    private GameObjectInstance[] instances;
    private EventSheet[] eventSheets;

    private final GameRuntime gameRuntime;
    private final Map<TextExpression, ExpressionParser> knownParser;

    public GameScene(GameRuntime aGameRuntime) {

        GameEventManager theManager = aGameRuntime.getEventManager();

        name = registerProperty(new Property<String>(this, NAME_PROPERTY, null, theManager));
        cameraObject = registerProperty(new Property<GameObject>(this, CAMERA_OBJECT_PROPERTY, null, theManager));
        defaultPlayer = registerProperty(new Property<GameObject>(this, DEFAULT_PLAYER_PROPERTY, null, theManager));
        backgroundColor = registerProperty(new Property<Color>(this, COLOR_PROPERTY, new Color(0, 0, 0), theManager));
        layoutBounds = registerProperty(new Property<Rectangle>(this, LAYOUT_BOUNDS_PROPERTY, new Rectangle(), theManager));
        instances = new GameObjectInstance[0];
        objects = new GameObject[0];
        eventSheets = new EventSheet[0];
        gameRuntime = aGameRuntime;

        knownParser = new HashMap<TextExpression, ExpressionParser>();
    }

    public ExpressionParser get(TextExpression aExpression) {
        ExpressionParser theParser = knownParser.get(aExpression);
        if (theParser == null) {
            theParser = gameRuntime.getExpressionParserFactory().create(aExpression);
            theParser.registerVariable(ExpressionParser.SCENE_VARIABLE, this);
            theParser.registerVariable(ExpressionParser.PLAYER_VARIABLE, defaultPlayer);
            theParser.registerVariable(ExpressionParser.CAMERA_VARIABLE, cameraObject);
            knownParser.put(aExpression, theParser);
        }
        return theParser;
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

    public Property<Rectangle> layoutBoundsProperty() {
        return layoutBounds;
    }

    public GameObject createNewGameObject(String aName) {
        GameObject theObject = new GameObject(this, aName);
        addGameObject(theObject);
        return theObject;
    }

    private void addGameObject(GameObject aObject) {
        List<GameObject> theObjects = ArrayUtils.asList(objects);
        if (theObjects.add(aObject)) {
            objects = theObjects.toArray(new GameObject[theObjects.size()]);
            gameRuntime.getEventManager().fire(new GameObjectAddedToScene(this, aObject));
        }
    }

    public void addGameObjectInstance(GameObjectInstance aInstance) {
        List<GameObjectInstance> theInstances = ArrayUtils.asList(instances);
        if (theInstances.add(aInstance)) {
            instances = theInstances.toArray(new GameObjectInstance[theInstances.size()]);
            gameRuntime.getEventManager().fire(new GameObjectInstanceAddedToScene(this, aInstance));
        }
    }

    public GameObjectInstance[] getInstances() {
        return instances;
    }

    public GameObject[] getObjects() {
        return objects;
    }

    public EventSheet[] getEventSheets() {
        return eventSheets;
    }

    public List<GameObjectInstance> findAllAt(Position aScreenPosition, Position aWorldPosition) {
        List<GameObjectInstance> theResult = new ArrayList<GameObjectInstance>();
        for (GameObjectInstance theInstance : instances) {
            if (theInstance.absolutePositionProperty().get()) {
                if (theInstance.contains(aScreenPosition)) {
                    theResult.add(theInstance);
                }
            } else {
                if (theInstance.contains(aWorldPosition)) {
                    theResult.add(theInstance);
                }
            }
        }
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

    public void removeGameObjectInstance(GameObjectInstance aInstance) {
        List<GameObjectInstance> theInstances = ArrayUtils.asList(instances);
        if (theInstances.remove(aInstance)) {
            instances = theInstances.toArray(new GameObjectInstance[theInstances.size()]);
            gameRuntime.getEventManager().fire(new GameObjectInstanceRemovedFromScene(this, aInstance));
        }
    }

    public void removeGameObject(GameObject aGameObject) {
        List<GameObjectInstance> theInstances = ArrayUtils.asList(instances);
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aGameObject) {
                removeGameObjectInstance(theInstance);
            }
        }
        List<GameObject> theObjects = ArrayUtils.asList(objects);
        if (theObjects.remove(aGameObject)) {
            objects = theObjects.toArray(new GameObject[theObjects.size()]);
            gameRuntime.getEventManager().fire(new GameObjectRemovedFromScene(this, aGameObject));
        }
    }

    public EventSheet createNewEventSheet() {
        EventSheet theSheet = new EventSheet(this);
        List<EventSheet> theSheets = ArrayUtils.asList(eventSheets);
        theSheets.add(theSheet);
        eventSheets = theSheets.toArray(new EventSheet[theSheets.size()]);

        gameRuntime.getEventManager().fire(new EventSheetAddedToScene(theSheet));
        return theSheet;
    }

    public void removeEventSheet(EventSheet aEventSheet) {
        List<EventSheet> theSheets = ArrayUtils.asList(eventSheets);
        if (theSheets.remove(aEventSheet)) {
            eventSheets = theSheets.toArray(new EventSheet[theSheets.size()]);
            gameRuntime.getEventManager().fire(new EventSheetRemovedFromScene(aEventSheet));
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(NAME_PROPERTY, name.get());

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

        theResult.put(LAYOUT_BOUNDS_PROPERTY, layoutBounds.get().serialize());
        return theResult;
    }

    public static GameScene deserialize(GameRuntime aGameRuntime, Map<String, Object> aSerializedData) {
        GameScene theScene = new GameScene(aGameRuntime);
        theScene.name.setQuietly((String) aSerializedData.get(NAME_PROPERTY));

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

        List<EventSheet> theEventSheetList = new ArrayList<EventSheet>();
        List<Map<String, Object>> theEventSheets = (List<Map<String, Object>>) aSerializedData.get("eventsheets");
        if (theEventSheets != null) {
            for (Map<String, Object> theSheet : theEventSheets) {
                theEventSheetList.add(EventSheet.unmarshall(aGameRuntime.getIORegistry(), theScene, theSheet));
            }
        }
        theScene.eventSheets = theEventSheetList.toArray(new EventSheet[theEventSheetList.size()]);

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
        Map<String, Object> theLayoutBounds = (Map<String, Object>) aSerializedData.get(LAYOUT_BOUNDS_PROPERTY);
        if (theLayoutBounds != null) {
            theScene.layoutBounds.setQuietly(Rectangle.deserialize(theLayoutBounds));
        }
        return theScene;
    }
}