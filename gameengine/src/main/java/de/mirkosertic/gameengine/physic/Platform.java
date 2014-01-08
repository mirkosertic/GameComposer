package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.event.Property;

public interface Platform {

    String MOVE_LEFT_KEY_PROPERTY = "moveLeftKey";
    String MOVE_RIGHT_KEY_PROPERTY = "moveRightKey";
    String JUMP_KEY_PROPERTY = "jumpKey";
    String LEFT_RIGHT_IMPULSE_PROPERTY = "leftRightImpulse";
    String JUMP_IMPULSE_PROPERTY = "jumpImpulse";
    String[] EDITABLE_PROPERTIES = {MOVE_LEFT_KEY_PROPERTY, MOVE_RIGHT_KEY_PROPERTY, JUMP_KEY_PROPERTY, LEFT_RIGHT_IMPULSE_PROPERTY, JUMP_IMPULSE_PROPERTY};

    Property<GameKeyCode> moveLeftKeyProperty();

    Property<GameKeyCode> moveRightKeyProperty();

    Property<GameKeyCode> jumpKeyProperty();

    Property<Float> leftRightImpulseProperty();

    Property<Float> jumpImpulseProperty();
}
