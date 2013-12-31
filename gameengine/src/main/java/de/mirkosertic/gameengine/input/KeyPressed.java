package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.types.GameKeyCode;

public class KeyPressed extends GameEvent {

    public final GameKeyCode keyCode;

    KeyPressed(GameKeyCode aKeyCode) {
        super("KeyPressed");
        keyCode = aKeyCode;
    }
}
