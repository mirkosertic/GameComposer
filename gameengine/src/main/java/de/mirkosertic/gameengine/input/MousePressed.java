package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.Position;

public class MousePressed extends GameEvent {

    public final Position position;

    public final GameObjectInstance[] affectedInstances;

    MousePressed(Position aPosition, GameObjectInstance[] aAffectedInstances) {
        super("MousePressed");
        position = aPosition;
        affectedInstances = aAffectedInstances;
    }
}
