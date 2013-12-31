package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectRemovedFromScene extends GameEvent {

    public final GameScene scene;
    public final GameObject object;

    public GameObjectRemovedFromScene(GameScene aScene, GameObject aObject) {
        super("GameObjectRemovedFromScene");
        scene = aScene;
        object = aObject;
    }
}