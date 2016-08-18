package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class GameObjectInstanceRemovedFromScene extends GameEvent implements DistributableEvent {

    public static final String EVENT_ID = "GameObjectInstanceRemovedFromScene";

    private static final GameObjectInstanceRemovedFromSceneClassInformation CIINSTANCE = new GameObjectInstanceRemovedFromSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceRemovedFromScene(GameScene aScene, GameObjectInstance aInstance) {
        super(EVENT_ID);
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameObjectInstanceRemovedFromSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type);
        theResult.put("instanceID", instance.uuidProperty().get());
        return theResult;
    }

    @Override
    public boolean isForced() {
        return true;
    }

    public static void runEventInScene(Map<String, Object> aEventData, GameScene aGameScene) {
        String theInstanceID = (String) aEventData.get("instanceID");
        GameObjectInstance theInstance = aGameScene.findInstanceByID(theInstanceID);
        if (theInstance != null) {
            aGameScene.removeGameObjectInstance(theInstance);
        }
    }
}