package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class KeyReleasedGameEvent extends GameEvent {

    private GameKeyCode keyCode;

    public KeyReleasedGameEvent(GameKeyCode aKeyCode) {
        keyCode = aKeyCode;
    }

    public GameKeyCode getKeyCode() {
        return keyCode;
    }
}
