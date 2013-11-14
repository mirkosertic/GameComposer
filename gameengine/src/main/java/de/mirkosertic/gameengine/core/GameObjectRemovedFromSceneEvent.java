package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectRemovedFromSceneEvent extends GameEvent {

    private final ReadOnlyProperty<GameScene> scene;
    private final ReadOnlyProperty<GameObject> object;

    public GameObjectRemovedFromSceneEvent(GameScene aScene, GameObject aObject) {
        super("GameObjectRemovedFromSceneEvent");
        scene = registerProperty(new ReadOnlyProperty<GameScene>(this, "scene", aScene));
        object = registerProperty(new ReadOnlyProperty<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }

    public ReadOnlyProperty<GameScene> sceneProperty() {
        return scene;
    }
}
