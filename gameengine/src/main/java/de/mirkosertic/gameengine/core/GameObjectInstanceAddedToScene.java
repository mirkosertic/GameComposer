package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectInstanceAddedToScene extends GameEvent {

    private final Property<GameScene> scene;
    private final Property<GameObjectInstance> instance;

    public GameObjectInstanceAddedToScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToScene");
        scene = registerProperty(new Property<GameScene>(this, "scene", aScene));
        instance = registerProperty(new Property<GameObjectInstance>(this, "instance", aInstance));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }

    public ReadOnlyProperty<GameScene> sceneProperty() {
        return scene;
    }
}