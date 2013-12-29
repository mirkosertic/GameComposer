package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.types.GameKeyCode;
import de.mirkosertic.gameengine.types.TouchPosition;

public interface GestureDetector {

    void keyPressed(GameKeyCode aKeyCode);

    void keyReleased(GameKeyCode aKeyCode);

    void touchStarted(TouchPosition[] aTouchPositions);

    void touchEnded(TouchPosition[] aTouchPositions);

    void touchMoved(TouchPosition[] aTouchPositions);

    void touchCanceled(TouchPosition[] aTouchPositions);
}
