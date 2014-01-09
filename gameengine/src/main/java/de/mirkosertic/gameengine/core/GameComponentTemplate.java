package de.mirkosertic.gameengine.core;

import java.util.Map;

public abstract class GameComponentTemplate<T extends GameComponent> {

    public abstract T create(GameObjectInstance aInstance, GameRuntime aGameRuntime);

    public abstract Map<String,Object> serialize();

    public abstract String getTypeKey();

    public abstract GameObject getOwner();
}