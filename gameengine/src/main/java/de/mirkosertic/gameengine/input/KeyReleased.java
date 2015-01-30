package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.type.GameKeyCode;

@InheritedClassInformation
public class KeyReleased extends GameEvent {

    private static final KeyReleasedClassInformation CIINSTANCE = new KeyReleasedClassInformation();

    @ReflectiveField
    public final GameKeyCode keyCode;

    KeyReleased(GameKeyCode aKeyCode) {
        super("KeyReleased");
        keyCode = aKeyCode;
    }

    @Override
    public KeyReleasedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}