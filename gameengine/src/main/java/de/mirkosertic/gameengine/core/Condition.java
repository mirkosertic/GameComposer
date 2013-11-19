package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

import java.util.Map;

public interface Condition {

    String TYPE_ATTRIBUTE = "type";

    ConditionResult appliesTo(GameEvent aEvent);

    Map<String, Object> serialize();
}