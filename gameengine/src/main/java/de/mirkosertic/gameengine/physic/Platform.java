package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.event.Property;

public interface Platform {

    String MOVE_LEFT_KEY_PROPERTY = "moveLeftKey";
    String MOVE_RIGHT_KEY_PROPERTY = "moveRightKey";
    String JUMP_KEY_PROPERTY = "jumpKey";
    String LEFT_RIGHT_IMPULSE_PROPERTY = "leftRightImpulse";
    String JUMP_IMPULSE_PROPERTY = "jumpImpulse";

    @ReflectiveField
    Property<GameKeyCode> moveLeftKeyProperty();

    @ReflectiveField
    Property<GameKeyCode> moveRightKeyProperty();

    @ReflectiveField
    Property<GameKeyCode> jumpKeyProperty();

    @ReflectiveField
    Property<Float> leftRightImpulseProperty();

    @ReflectiveField
    Property<Float> jumpImpulseProperty();

    @ReflectiveMethod
    void delete();
}
