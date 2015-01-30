package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;
import de.mirkosertic.gameengine.type.Position;

@InheritedClassInformation
public class MousePressed extends GameEvent {

    private static final MousePressedClassInformation CIINSTANCE = new MousePressedClassInformation();

    @ReflectiveField
    public final Position position;

    @ReflectiveField
    public final GameObjectInstance[] affectedInstances;

    MousePressed(Position aPosition, GameObjectInstance[] aAffectedInstances) {
        super("MousePressed");
        position = aPosition;
        affectedInstances = aAffectedInstances;
    }

    @Override
    public MousePressedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
