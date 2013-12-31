package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class GameObjectConfigurationChanged extends GameEvent {

    public final GameObject object;

    public GameObjectConfigurationChanged(GameObject aObject) {
        super("GameObjectConfigurationChanged");
        object = aObject;
    }
}