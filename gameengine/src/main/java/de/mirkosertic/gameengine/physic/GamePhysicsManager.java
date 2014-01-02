package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.GameSystem;

public interface GamePhysicsManager extends GameSystem {

    void drawDebug(PhysicsDebugCanvas aCanvas);
}