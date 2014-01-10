package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface Behavior {

    String TYPE_ATTRIBUTE = "type";

    Map<String, Object> serialize();

    String getTypeKey();

    <T extends BehaviorTemplate> T getTemplate();
}
