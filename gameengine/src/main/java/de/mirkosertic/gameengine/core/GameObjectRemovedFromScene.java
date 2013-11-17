package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectRemovedFromScene extends GameEvent {

    private final Property<GameScene> scene;
    private final Property<GameObject> object;

    public GameObjectRemovedFromScene(GameScene aScene, GameObject aObject) {
        super("GameObjectRemovedFromScene");
        scene = registerProperty(new Property<GameScene>(this, "scene", aScene));
        object = registerProperty(new Property<GameObject>(this, "object", aObject));
    }

    public ReadOnlyProperty<GameObject> objectProperty() {
        return object;
    }

    public ReadOnlyProperty<GameScene> sceneProperty() {
        return scene;
    }
}
