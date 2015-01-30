package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class GameObjectConfigurationChanged extends GameEvent {

    private static final GameObjectConfigurationChangedClassInformation CIINSTANCE = new GameObjectConfigurationChangedClassInformation();

    @ReflectiveField
    public final GameObject object;

    public GameObjectConfigurationChanged(GameObject aObject) {
        super("GameObjectConfigurationChanged");
        object = aObject;
    }

    @Override
    public GameObjectConfigurationChangedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}