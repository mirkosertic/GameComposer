package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class RunScene extends GameEvent {

    public final String sceneId;

    public RunScene(String aSceneId) {
        super("RunScene");
        sceneId = aSceneId;
    }
}