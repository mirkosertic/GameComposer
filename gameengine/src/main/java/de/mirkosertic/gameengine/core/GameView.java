package de.mirkosertic.gameengine.core;

public interface GameView {

    void renderGame(long aGameTime, long aElapsedTimeSinceLastLoop, GameScene aScene);
}
