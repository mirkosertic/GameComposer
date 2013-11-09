package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public interface Action {

    void invoke(GameScene aScene, GameEvent aEvent);
}
