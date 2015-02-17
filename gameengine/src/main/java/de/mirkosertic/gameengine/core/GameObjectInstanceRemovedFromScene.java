package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class GameObjectInstanceRemovedFromScene extends GameEvent implements DistributableEvent {

    private static final GameObjectInstanceRemovedFromSceneClassInformation CIINSTANCE = new GameObjectInstanceRemovedFromSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceRemovedFromScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceRemovedFromScene");
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
}
