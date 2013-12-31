package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectInstanceRemovedFromScene extends GameEvent {

    public final GameScene scene;
    public final GameObjectInstance instance;

    public GameObjectInstanceRemovedFromScene(GameScene aScene, GameObjectInstance aInstance) {
        super("GameObjectInstanceRemovedFromScene");
        scene = aScene;
        instance = aInstance;
    }
}
