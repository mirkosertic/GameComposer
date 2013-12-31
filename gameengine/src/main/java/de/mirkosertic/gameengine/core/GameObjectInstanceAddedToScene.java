package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectInstanceAddedToScene extends GameEvent {

    public final GameScene scene;
    public final GameObjectInstance instance;

    public GameObjectInstanceAddedToScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceAddedToScene");
        scene = aScene;
        instance = aInstance;
    }
}