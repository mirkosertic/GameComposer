package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameSystem;

public interface GamePhysicsManager extends GameSystem {

    void drawDebug(PhysicsDebugCanvas aCanvas);
}