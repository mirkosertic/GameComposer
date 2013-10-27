package de.mirkosertic.gameengine.core;

public class KeyReleasedGameEvent extends GameEvent {

    private GameKeyCode keyCode;

    public KeyReleasedGameEvent(GameKeyCode aKeyCode) {
        keyCode = aKeyCode;
    }

    public GameKeyCode getKeyCode() {
        return keyCode;
    }
}
