package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public interface Condition {

    boolean appliesTo(GameEvent aEvent);
}