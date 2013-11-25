package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class RunScene extends GameEvent {

    private final Property<String> sceneId;

    public RunScene(String aSceneId) {
        super("RunScene");
        sceneId = registerProperty(new Property<String>(this, "sceneId", aSceneId));
    }

    public ReadOnlyProperty<String> sceneIdProperty() {
        return sceneId;
    }
}