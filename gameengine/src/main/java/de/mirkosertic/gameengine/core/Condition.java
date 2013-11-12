package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

import java.util.Map;

public interface Condition {

    String TYPE_ATTRIBUTE = "type";

    boolean appliesTo(GameEvent aEvent);

    Map<String, Object> serialize();
}