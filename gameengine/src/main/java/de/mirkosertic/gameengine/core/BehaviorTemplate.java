package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface BehaviorTemplate<T extends Behavior> {

    T create(GameObjectInstance aInstance, GameRuntime aGameRuntime);

    Map<String,Object> serialize();

    String getTypeKey();

    GameObject getOwner();
}