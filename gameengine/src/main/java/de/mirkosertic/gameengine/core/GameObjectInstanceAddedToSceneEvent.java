package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameObjectInstanceAddedToSceneEvent extends GameEvent {

    private final ReadOnlyProperty<GameScene> scene;
    private final ReadOnlyProperty<GameObjectInstance> instance;

    public GameObjectInstanceAddedToSceneEvent(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToSceneEvent");
        scene = registerProperty(new ReadOnlyProperty<GameScene>(this, "scene", aScene));
        instance = registerProperty(new ReadOnlyProperty<GameObjectInstance>(this, "instance", aInstance));
    }

    public ReadOnlyProperty<GameObjectInstance> instanceProperty() {
        return instance;
    }

    public ReadOnlyProperty<GameScene> sceneProperty() {
        return scene;
    }
}