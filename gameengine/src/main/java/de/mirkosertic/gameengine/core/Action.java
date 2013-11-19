package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface Action {

    String TYPE_ATTRIBUTE = "type";

    void invoke(GameScene aScene, ConditionResult aResult);

    Map<String,Object> serialize();
}
