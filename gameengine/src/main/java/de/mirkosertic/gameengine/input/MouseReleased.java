package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;
import de.mirkosertic.gameengine.type.Position;

@InheritedClassInformation
public class MouseReleased extends GameEvent {

    private static final MouseReleasedClassInformation CIINSTANCE = new MouseReleasedClassInformation();

    @ReflectiveField
    public final Position position;

    @ReflectiveField
    public final GameObjectInstance[] affectedInstances;

    MouseReleased(Position aPosition, GameObjectInstance[] aAffectedInstances) {
        super("MouseReleased");
        position = aPosition;
        affectedInstances = aAffectedInstances;
    }

    @Override
    public MouseReleasedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
