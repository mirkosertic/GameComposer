package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.DistributableEvent;
import de.mirkosertic.gameengine.event.GameEvent;

import java.util.HashMap;
import java.util.Map;

@InheritedClassInformation
public class GameObjectInstanceAddedToScene extends GameEvent implements DistributableEvent {

    private static final GameObjectInstanceAddedToSceneClassInformation CIINSTANCE = new GameObjectInstanceAddedToSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObjectInstance instance;

    public GameObjectInstanceAddedToScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToScene");
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameObjectInstanceAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(EVENT_ID_ATTRIBUTE, type);
        theResult.put("newInstanceID", instance.uuidProperty().get());
        theResult.put("gameObject", instance.getOwnerGameObject().uuidProperty().get());
        return theResult;
    }
}