package de.mirkosertic.gameengine.core;

import java.util.Map;

public interface GameComponentTemplate<T extends GameComponent> {

    T create(GameObjectInstance aInstance, GameRuntime aGameRuntime);

    Map<String,Object> serialize();

    String getTypeKey();
}
