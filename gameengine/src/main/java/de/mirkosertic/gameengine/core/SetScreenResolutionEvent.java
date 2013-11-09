package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;

public class SetScreenResolutionEvent extends GameEvent {

    private int screenWidth;
    private int screenHeight;

    public SetScreenResolutionEvent(int aScreenWidth, int aScreenHeight) {
        screenWidth = aScreenWidth;
        screenHeight = aScreenHeight;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }
}
