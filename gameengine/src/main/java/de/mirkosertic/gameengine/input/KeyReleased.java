package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.types.GameKeyCode;

public class KeyReleased extends GameEvent {

    public final GameKeyCode keyCode;

    KeyReleased(GameKeyCode aKeyCode) {
        super("KeyReleased");
        keyCode = aKeyCode;
    }
}