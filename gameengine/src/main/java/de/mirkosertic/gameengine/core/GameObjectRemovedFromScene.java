package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class GameObjectRemovedFromScene extends GameEvent {

    private static final GameObjectRemovedFromSceneClassInformation CIINSTANCE = new GameObjectRemovedFromSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObject object;

    public GameObjectRemovedFromScene(GameScene aScene, GameObject aObject) {
        super("GameObjectRemovedFromScene");
        scene = aScene;
        object = aObject;
    }

    @Override
    public GameObjectRemovedFromSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}