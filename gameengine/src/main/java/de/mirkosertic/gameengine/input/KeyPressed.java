package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;
import de.mirkosertic.gameengine.type.GameKeyCode;

@InheritedClassInformation
public class KeyPressed extends GameEvent {

    private static final KeyPressedClassInformation CIINSTANCE = new KeyPressedClassInformation();

    @ReflectiveField
    public final GameKeyCode keyCode;

    KeyPressed(GameKeyCode aKeyCode) {
        super("KeyPressed");
        keyCode = aKeyCode;
    }

    @Override
    public KeyPressedClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
