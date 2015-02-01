package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class GameObjectInstanceRemovedFromScene extends GameEvent {

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
}
