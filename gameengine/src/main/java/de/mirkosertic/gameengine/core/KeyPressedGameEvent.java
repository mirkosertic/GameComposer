package de.mirkosertic.gameengine.core;

public class KeyPressedGameEvent extends GameEvent {

    private GameKeyCode keyCode;

    public KeyPressedGameEvent(GameKeyCode aKeyCode) {
        keyCode = aKeyCode;
    }

    public GameKeyCode getKeyCode() {
        return keyCode;
    }
}
