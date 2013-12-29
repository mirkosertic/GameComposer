package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.event.Property;

public interface Platform {

    Property<GameKeyCode> moveLeftKeyProperty();

    Property<GameKeyCode> moveRightKeyProperty();

    Property<GameKeyCode> jumpKeyProperty();

    Property<Float> leftRightImpulseProperty();

    Property<Float> jumpImpulseProperty();
}
