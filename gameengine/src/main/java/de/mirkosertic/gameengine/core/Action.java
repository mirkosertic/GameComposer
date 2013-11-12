package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

import java.util.Map;

public interface Action {

    String TYPE_ATTRIBUTE = "type";

    void invoke(GameScene aScene, GameEvent aEvent);

    Map<String,Object> serialize();
}
