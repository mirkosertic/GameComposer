package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameEventManager;

public interface GamePhysicsManagerFactory {

    GamePhysicsManager create(GameEventManager aEventManager);
}
