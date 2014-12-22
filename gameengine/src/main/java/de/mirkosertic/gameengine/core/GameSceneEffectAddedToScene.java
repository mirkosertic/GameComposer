package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameSceneEffectAddedToScene extends GameEvent {

    public final GameScene scene;
    public final GameSceneEffect instance;

    public GameSceneEffectAddedToScene(GameScene aScene, GameSceneEffect aInstance) {
        super("SceneEffectAddedToScene");
        scene = aScene;
        instance = aInstance;
    }
}