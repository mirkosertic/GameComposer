package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface Behavior {

    String TYPE_ATTRIBUTE = "type";

    String getType();

    GameObjectInstance getInstance();

    Map<String, Object> serialize();

    <T extends BehaviorTemplate> T getTemplate();
}
