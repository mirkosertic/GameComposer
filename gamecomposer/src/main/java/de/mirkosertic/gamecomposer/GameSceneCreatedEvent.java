package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameScene;

public class GameSceneCreatedEvent {

    private final GameScene gameScene;

    public GameSceneCreatedEvent(GameScene aScene) {
        gameScene = aScene;
    }

    public GameScene getGameScene() {
        return gameScene;
    }
}