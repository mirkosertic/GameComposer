package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameSystem;

public interface GamePhysicsManager extends GameSystem {

    void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop);

    void drawDebug(PhysicsDebugCanvas aCanvas);
}