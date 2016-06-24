package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.event.GameEventManager;

public interface GamePhysicsManagerFactory {

    GamePhysicsManager create(GameEventManager aEventManager, ThreadingManager aThreadingManager);
}
