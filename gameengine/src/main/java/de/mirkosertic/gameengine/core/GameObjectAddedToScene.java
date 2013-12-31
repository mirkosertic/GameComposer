package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectAddedToScene extends GameEvent {

    public final GameScene scene;
    public final GameObject object;

    public GameObjectAddedToScene(GameScene aScene, GameObject aObject) {
        super("GameObjectAddedToScene");
        scene = aScene;
        object = aObject;
    }
}