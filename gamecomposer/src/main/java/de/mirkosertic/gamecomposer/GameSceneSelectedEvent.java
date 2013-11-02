package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameScene;

public class GameSceneSelectedEvent {

    private GameScene scene;

    public GameSceneSelectedEvent(GameScene aScene) {
        scene = aScene;
    }

    public GameScene getScene() {
        return  scene;
    }
}
