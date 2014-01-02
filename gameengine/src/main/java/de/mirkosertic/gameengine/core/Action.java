package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface Action {

    String TYPE_ATTRIBUTE = "type";

    String PROPERTY_MOVEMENT_DIRECTION = "movementDirection";
    String PROPERTY_MOVEMENT_FORCE = "movementForce";

    void invoke(GameScene aScene, ConditionResult aResult);

    Map<String,Object> serialize();
}
