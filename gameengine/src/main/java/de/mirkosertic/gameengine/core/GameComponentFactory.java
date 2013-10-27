package de.mirkosertic.gameengine.core;

public interface GameComponentFactory<T extends GameComponent> {

    T create(GameObjectInstance aInstance, GameRuntime aGameRuntime);
}
