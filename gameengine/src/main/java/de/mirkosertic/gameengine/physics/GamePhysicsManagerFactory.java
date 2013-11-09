package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.event.GameEventManager;

public interface GamePhysicsManagerFactory {

    GamePhysicsManager create(GameEventManager aEventManager);
}
