package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class RunScene extends GameEvent {

    private static final RunSceneClassInformation CIINSTANCE = new RunSceneClassInformation();

    @ReflectiveField
    public final String sceneId;

    public RunScene(String aSceneId) {
        super("RunScene");
        sceneId = aSceneId;
    }

    @Override
    public RunSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}