package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class SceneStarted extends GameEvent {

    public SceneStarted() {
        super("SceneStartup");
    }
}