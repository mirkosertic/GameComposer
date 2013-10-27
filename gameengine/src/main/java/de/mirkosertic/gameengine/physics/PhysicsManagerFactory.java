package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameEventManager;

public interface PhysicsManagerFactory {

    GamePhysicsManager create(GameEventManager aEventManager);
}
