package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameSceneEffectRemovedFromScene extends GameEvent {

    public final GameScene scene;
    public final GameSceneEffect instance;

    public GameSceneEffectRemovedFromScene(GameScene aScene, GameSceneEffect aInstance) {
        super("SceneEffectRemovedFromScene");
        scene = aScene;
        instance = aInstance;
    }
}