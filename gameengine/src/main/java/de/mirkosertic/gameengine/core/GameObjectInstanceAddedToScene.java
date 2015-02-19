package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Position;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class GameObjectInstanceAddedToScene extends GameEvent implements DistributableEvent {

    public static final String EVENT_ID = "GameObjectInstanceAddedToScene";

    private static final GameObjectInstanceAddedToSceneClassInformation CIINSTANCE = new GameObjectInstanceAddedToSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceAddedToScene(GameScene aScene, GameObjectInstance aInstance) {
        super(EVENT_ID);
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameObjectInstanceAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public boolean isForced() {
        return true;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, EVENT_ID);
        theResult.put("newInstanceID", instance.uuidProperty().get());
        theResult.put("gameObject", instance.getOwnerGameObject().uuidProperty().get());
        theResult.put("initialPosition", instance.positionProperty().get().serialize());
        return theResult;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        String theGameObjectID = (String) aEventData.get("gameObject");
        String theInstanceID = (String) aEventData.get("newInstanceID");
        if (theGameObjectID != null && theInstanceID != null) {

            GameObjectInstance theInstance = aGameScene.findInstanceByID(theInstanceID);
            if (theInstance != null) {
                // Object exists, we are done here
                return;
            }

            GameObject theObject = aGameScene.findObjectByID(theGameObjectID);
            if (theObject != null) {
                GameObjectInstance theNewInstance = aGameScene.createFrom(theObject);
                theNewInstance.uuidProperty().set(theInstanceID);
                theNewInstance.positionProperty().set(Position.deserialize(
                        (Map<String, Object>) aEventData.get("initialPosition")));

                theNewInstance.markAsRemoteObject();

                aGameScene.addInstance(theNewInstance);
            }
        }
    }
}