package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.event.Property;

public interface Platform {

    String MOVE_LEFT_KEY_PROPERTY = "moveLeftKey";
    String MOVE_RIGHT_KEY_PROPERTY = "moveRightKey";
    String JUMP_KEY_PROPERTY = "jumpKey";
    String LEFT_RIGHT_IMPULSE_PROPERTY = "leftRightImpulse";
    String JUMP_IMPULSE_PROPERTY = "jumpImpulse";

    Property<GameKeyCode> moveLeftKeyProperty();

    Property<GameKeyCode> moveRightKeyProperty();

    Property<GameKeyCode> jumpKeyProperty();

    Property<Float> leftRightImpulseProperty();

    Property<Float> jumpImpulseProperty();
}
