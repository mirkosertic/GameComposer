/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.ArrayUtils;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.CustomProperties;
import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.PositionAnchor;
import de.mirkosertic.gameengine.type.Rectangle;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameScene implements Reflectable<GameSceneClassInformation>, KeyValueObjectCache {

    private static final GameSceneClassInformation CIINSTANCE = new GameSceneClassInformation();

    public static final String NAME_PROPERTY = "name";
    public static final String CAMERA_OBJECT_PROPERTY = "cameraObject";
    public static final String DEFAULT_PLAYER_PROPERTY = "defaultPlayer";
    public static final String COLOR_PROPERTY = "color";
    public static final String LAYOUT_BOUNDS_PROPERTY = "layoutBounds";
    public static final String CUSTOM_PROPERTIES_PROPERTY = "customProperties";

    private final Property<String> name;
    private final Property<GameObject> cameraObject;
    private final Property<GameObject> defaultPlayer;
    private final Property<Color> backgroundColor;
    private final Property<Rectangle> layoutBounds;
    private final Property<CustomProperties> customProperties;

    private GameObject[] objects;
    private GameObjectInstance[] instances;
    private EventSheet[] eventSheets;
    private Spritesheet[] spriteSheets;

    private GameSceneEffect[] preprocessorEffects;
    private GameSceneEffect[] postprocessorEffects;

    private final GameRuntime gameRuntime;

    private final Game game;

    private final Map<Object, Object> keyValueStore;

    public GameScene(Game aGame, GameRuntime aGameRuntime) {

        keyValueStore = new HashMap<>();
        game = aGame;
        GameEventManager theManager = aGameRuntime.getEventManager();

        name = new Property<>(String.class, this, NAME_PROPERTY, null, theManager);
        cameraObject = new Property<>(GameObject.class, this, CAMERA_OBJECT_PROPERTY, null, theManager);
        defaultPlayer = new Property<>(GameObject.class, this, DEFAULT_PLAYER_PROPERTY, null, theManager);
        backgroundColor = new Property<>(Color.class, this, COLOR_PROPERTY, new Color(0, 0, 0), theManager);
        layoutBounds = new Property<>(Rectangle.class, this, LAYOUT_BOUNDS_PROPERTY, new Rectangle(), theManager);
        customProperties = new Property<>(CustomProperties.class, this, CUSTOM_PROPERTIES_PROPERTY, new CustomProperties(), theManager);
        instances = new GameObjectInstance[0];
        objects = new GameObject[0];
        spriteSheets = new Spritesheet[0];
        eventSheets = new EventSheet[0];
        preprocessorEffects = new GameSceneEffect[0];
        postprocessorEffects = new GameSceneEffect[0];
        gameRuntime = aGameRuntime;
    }

    @Override
    public GameSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    public GameRuntime getRuntime() {
        return gameRuntime;
    }

    public Game getGame() {
        return game;
    }

    @ReflectiveField
    public Property<String> nameProperty() {
        return name;
    }

    @ReflectiveField
    public Property<GameObject> cameraObjectProperty() {
        return cameraObject;
    }

    @ReflectiveField
    public Property<Color> backgroundColorProperty() {
        return backgroundColor;
    }

    @ReflectiveField
    public Property<GameObject> defaultPlayerProperty() {
        return defaultPlayer;
    }

    @ReflectiveField
    public Property<Rectangle> layoutBoundsProperty() {
        return layoutBounds;
    }

    @ReflectiveField
    public Property<CustomProperties> customPropertiesProperty() {
        return customProperties;
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

    @ReflectiveMethod
    public void addInstance(GameObjectInstance aInstance) {
        List<GameObjectInstance> theInstances = ArrayUtils.asList(instances);
        if (theInstances.add(aInstance)) {
            instances = theInstances.toArray(new GameObjectInstance[theInstances.size()]);
            gameRuntime.getEventManager().fire(new GameObjectInstanceAddedToScene(aInstance));
        }
    }

    public void addEffect(GameSceneEffect aInstance) {
        switch (aInstance.getEffectType()) {
            case PREPROCESSOR:
                List<GameSceneEffect> thePreEffects = ArrayUtils.asList(preprocessorEffects);
                if (thePreEffects.add(aInstance)) {
                    preprocessorEffects = thePreEffects.toArray(new GameSceneEffect[thePreEffects.size()]);
                    gameRuntime.getEventManager().fire(new GameSceneEffectAddedToScene(this, aInstance));
                }
                break;
            case POSTPROCESSOR:
                List<GameSceneEffect> thePostEffects = ArrayUtils.asList(postprocessorEffects);
                if (thePostEffects.add(aInstance)) {
                    postprocessorEffects = thePostEffects.toArray(new GameSceneEffect[thePostEffects.size()]);
                    gameRuntime.getEventManager().fire(new GameSceneEffectAddedToScene(this, aInstance));
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown effect type : " + aInstance.getEffectType());
        }
    }

    public void removeEffect(GameSceneEffect aInstance) {
        switch (aInstance.getEffectType()) {
            case PREPROCESSOR:
                List<GameSceneEffect> thePreEffects = ArrayUtils.asList(preprocessorEffects);
                if (thePreEffects.remove(aInstance)) {
                    preprocessorEffects = thePreEffects.toArray(new GameSceneEffect[thePreEffects.size()]);
                    gameRuntime.getEventManager().fire(new GameSceneEffectRemovedFromScene(this, aInstance));
                }
                break;
            case POSTPROCESSOR:
                List<GameSceneEffect> thePostEffects = ArrayUtils.asList(postprocessorEffects);
                if (thePostEffects.remove(aInstance)) {
                    postprocessorEffects = thePostEffects.toArray(new GameSceneEffect[thePostEffects.size()]);
                    gameRuntime.getEventManager().fire(new GameSceneEffectRemovedFromScene(this, aInstance));
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown effect type : " + aInstance.getEffectType());
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

    public Spritesheet[] getSpriteSheets() {
        return spriteSheets;
    }

    public GameSceneEffect[] getPreprocessorEffects() {
        return preprocessorEffects;
    }

    public GameSceneEffect[] getPostprocessorEffects() {
        return postprocessorEffects;
    }

    public GameObject findObjectByID(String aObjectUUID) {
        for (GameObject theObject : objects) {
            if (theObject.uuidProperty().get().equals(aObjectUUID)) {
                return theObject;
            }
        }
        return null;
    }

    @ReflectiveMethod
    public GameObject findObjectByName(String aObjectName) {
        for (GameObject theObject : objects) {
            if (theObject.nameProperty().get().equals(aObjectName)) {
                return theObject;
            }
        }
        return null;
    }

    @ReflectiveMethod
    public GameObjectInstance findInstanceByName(String aName) {
        for (GameObjectInstance theInstance : instances) {
            if (aName.equals(theInstance.nameProperty().get())) {
                return theInstance;
            }
        }
        return null;
    }

    @ReflectiveMethod
    public GameObjectInstance findInstanceByID(String aInstanceID) {
        for (GameObjectInstance theInstance : instances) {
            if (aInstanceID.equals(theInstance.uuidProperty().get())) {
                return theInstance;
            }
        }
        return null;
    }

    @ReflectiveMethod
    public GameObjectInstance createFrom(GameObject aGameObject) {
        GameObjectInstance theInstance = new GameObjectInstance(gameRuntime.getEventManager(), aGameObject);
        theInstance.nameProperty().setQuietly(aGameObject.nameProperty().get() + "_" + System.currentTimeMillis());
        for (BehaviorTemplate theFactory : aGameObject.getBehaviorTemplates()) {
            theInstance.addBehavior(theFactory.create(theInstance, gameRuntime));
        }
        return theInstance;
    }

    @ReflectiveMethod
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

    public Spritesheet createNewSpriteSheet() {
        Spritesheet theSheet = new Spritesheet(this);
        List<Spritesheet> theSheets = ArrayUtils.asList(spriteSheets);
        theSheets.add(theSheet);
        spriteSheets = theSheets.toArray(new Spritesheet[theSheets.size()]);

        gameRuntime.getEventManager().fire(new SpriteSheetAddedToScene(theSheet));
        return theSheet;
    }

    public void removeEventSheet(EventSheet aEventSheet) {
        List<EventSheet> theSheets = ArrayUtils.asList(eventSheets);
        if (theSheets.remove(aEventSheet)) {
            eventSheets = theSheets.toArray(new EventSheet[theSheets.size()]);
            gameRuntime.getEventManager().fire(new EventSheetRemovedFromScene(aEventSheet));
        }
    }

    public void removeSpriteSheet(Spritesheet aSpriteSheet) {
        List<Spritesheet> theSheets = ArrayUtils.asList(spriteSheets);
        if (theSheets.remove(aSpriteSheet)) {
            spriteSheets = theSheets.toArray(new Spritesheet[theSheets.size()]);
            gameRuntime.getEventManager().fire(new SpriteSheetRemovedFromScene(aSpriteSheet));
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, name.get());

        List<Map<String, Object>> theObjects = new ArrayList<>();
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

        List<Map<String, Object>> theInstances = new ArrayList<>();
        for (GameObjectInstance theInstance : instances) {
            theInstances.add(theInstance.serialize());
        }
        theResult.put("instances", theInstances);

        List<Map<String, Object>> theEventSheets = new ArrayList<>();
        for (EventSheet theSheet : eventSheets) {
            theEventSheets.add(theSheet.serialize());
        }
        theResult.put("eventsheets", theEventSheets);

        List<Map<String, Object>> theSpriteSheets = new ArrayList<>();
        for (Spritesheet theSheet : spriteSheets) {
            theSpriteSheets.add(theSheet.serialize());
        }
        theResult.put("spritesheets", theSpriteSheets);

        theResult.put(LAYOUT_BOUNDS_PROPERTY, layoutBounds.get().serialize());

        List<Map<String, Object>> theEffects = new ArrayList<>();
        for (GameSceneEffect theEffect : preprocessorEffects) {
            theEffects.add(theEffect.serialize());
        }
        for (GameSceneEffect theEffect : postprocessorEffects) {
            theEffects.add(theEffect.serialize());
        }
        theResult.put("effects", theEffects);

        theResult.put("customProperties", customProperties.get().serialize());

        return theResult;
    }

    public static GameScene deserialize(Game aGame, GameRuntime aGameRuntime, Map<String, Object> aSerializedData) {
        GameScene theScene = new GameScene(aGame, aGameRuntime);
        theScene.name.setQuietly((String) aSerializedData.get(NAME_PROPERTY));

        aGameRuntime.getLogger().info("Loading objects");
        List<Map<String, Object>> theObjects = (List<Map<String, Object>>) aSerializedData.get("objects");
        for (Map<String, Object> theObject : theObjects) {
            theScene.addGameObject(GameObject.deserialize(aGameRuntime, theScene, theObject));
        }

        aGameRuntime.getLogger().info("Loading instances");
        List<Map<String, Object>> theInstances = (List<Map<String, Object>>) aSerializedData.get("instances");
        if (theInstances != null) {
            for (Map<String, Object> theInstance : theInstances) {
                theScene.addInstance(GameObjectInstance.deserialize(aGameRuntime, theScene, theInstance));
            }
        }

        aGameRuntime.getLogger().info("Loading event sheets");
        List<EventSheet> theEventSheetList = new ArrayList<>();
        List<Map<String, Object>> theEventSheets = (List<Map<String, Object>>) aSerializedData.get("eventsheets");
        if (theEventSheets != null) {
            for (Map<String, Object> theSheet : theEventSheets) {
                theEventSheetList.add(EventSheet.unmarshall(aGameRuntime.getIORegistry(), theScene, theSheet));
            }
        }
        theScene.eventSheets = theEventSheetList.toArray(new EventSheet[theEventSheetList.size()]);

        aGameRuntime.getLogger().info("Loading sprite sheets");
        List<Spritesheet> theSpriteSheetList = new ArrayList<>();
        List<Map<String, Object>> theSpriteSheets = (List<Map<String, Object>>) aSerializedData.get("spritesheets");
        if (theSpriteSheets != null) {
            for (Map<String, Object> theSheet : theSpriteSheets) {
                theSpriteSheetList.add(Spritesheet.unmarshall(theScene, theSheet));
            }
        }
        theScene.spriteSheets = theSpriteSheetList.toArray(new Spritesheet[theSpriteSheetList.size()]);

        aGameRuntime.getLogger().info("Loading core properties");
        String theCameraObject = (String) aSerializedData.get("cameraobjectid");
        if (theCameraObject != null) {
            theScene.cameraObject.setQuietly(theScene.findObjectByID(theCameraObject));
        }
        String theDefaultPlayerObject = (String) aSerializedData.get("defaultplayerobjectid");
        if (theDefaultPlayerObject != null) {
            theScene.defaultPlayer.setQuietly(theScene.findObjectByID(theDefaultPlayerObject));
        }
        Map<String, Object> theBackgroundColor = (Map<String, Object>) aSerializedData.get("backgroundcolor");
        if (theBackgroundColor != null) {
            theScene.backgroundColor.setQuietly(Color.deserialize(theBackgroundColor));
        }
        Map<String, Object> theLayoutBounds = (Map<String, Object>) aSerializedData.get(LAYOUT_BOUNDS_PROPERTY);
        if (theLayoutBounds != null) {
            theScene.layoutBounds.setQuietly(Rectangle.deserialize(theLayoutBounds));
        }

        aGameRuntime.getLogger().info("Loading effects");
        List<Map<String, Object>> theEffects = (List<Map<String, Object>>) aSerializedData.get("effects");
        if (theEffects != null) {
            for (Map<String, Object> theData : theEffects) {
                String theType = (String) theData.get(GameSceneEffect.TYPE_ATTRIBUTE);
                theScene.addEffect(aGameRuntime.getIORegistry().getSceneEffectUnmarshallerFor(theType).unmarshall(aGameRuntime, theScene, theData));
            }
        }

        aGameRuntime.getLogger().info("Loading custom properties");
        Map<String, Object> theCustomProperties = (Map<String, Object>) aSerializedData.get("customProperties");
        if (theCustomProperties != null) {
            theScene.customProperties.setQuietly(CustomProperties.deserialize(theCustomProperties));
        }

        return theScene;
    }

    public Position computeCenter() {
        if (instances.length == 0) {
            return new Position(0, 0);
        }
        int theMinX = Integer.MAX_VALUE;
        int theMinY = Integer.MAX_VALUE;
        int theMaxX = Integer.MIN_VALUE;
        int theMaxY = Integer.MIN_VALUE;
        for (GameObjectInstance theInstance : instances) {
            // Ignore instances with absolute positioning
            if (theInstance.positionAnchorProperty().get() == PositionAnchor.SCENE) {
                Position thePosition = theInstance.positionProperty().get();
                Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();
                theMinX = Math.min(theMinX, (int) thePosition.x);
                theMinY = Math.min(theMinY, (int) thePosition.y);
                theMaxX = Math.max(theMaxX, (int) thePosition.x + theSize.width);
                theMaxY = Math.max(theMaxY, (int) thePosition.y + theSize.height);
            }
        }
        return new Position(theMinX + (theMaxX - theMinX) / 2, theMinY + (theMaxY - theMinY) / 2);
    }

    public void removeBehaviorFrom(GameObject aObject, Behavior aBehavior) {
        GameObjectInstance[] theInstances = getInstances();
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aObject) {
                theInstance.removeBehavior(aBehavior);
            }
        }
        aObject.remove(aBehavior.getTemplate());
    }

    public void removeBehaviorFrom(GameObject aObject, BehaviorTemplate aBehaviorTemplate) {
        GameObjectInstance[] theInstances = getInstances();
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aObject) {
                theInstance.removeBehaviorByTemplate(aBehaviorTemplate);
            }
        }
        aObject.remove(aBehaviorTemplate);
    }

    public <T extends Behavior> void addBehaviorToObject(GameObject aObject, BehaviorTemplate<T> aBehaviorTemplate) {
        GameObjectInstance[] theInstances = getInstances();
        for (GameObjectInstance theInstance : theInstances) {
            if (theInstance.getOwnerGameObject() == aObject) {
                theInstance.addBehavior(aBehaviorTemplate.create(theInstance, gameRuntime));
            }
        }
        aObject.add(aBehaviorTemplate);
    }

    @Override
    public <T> T getObjectForKey(Object aKey) {
        return (T) keyValueStore.get(aKey);
    }

    @Override
    public <T> void setObjectForKey(Object aKey, T aValue) {
        keyValueStore.put(aKey, aValue);
    }
}