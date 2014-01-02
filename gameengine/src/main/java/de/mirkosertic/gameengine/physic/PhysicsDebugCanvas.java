package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.Position;

public interface PhysicsDebugCanvas {

    void drawLine(Position p1, Position p2, boolean aAwake);

    void drawPosition(Position p);
}
