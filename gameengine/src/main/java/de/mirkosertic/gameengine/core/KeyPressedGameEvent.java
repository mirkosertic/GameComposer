package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class KeyPressedGameEvent extends GameEvent {

    private GameKeyCode keyCode;

    public KeyPressedGameEvent(GameKeyCode aKeyCode) {
        keyCode = aKeyCode;
    }

    public GameKeyCode getKeyCode() {
        return keyCode;
    }
}
