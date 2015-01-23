package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.TouchPosition;

public interface GestureDetector {

    void keyPressed(GameKeyCode aKeyCode);

    void keyReleased(GameKeyCode aKeyCode);

    void mousePressed(Position aMousePosition);

    void mouseReleased(Position aMousePosition);

    void touchStarted(TouchPosition[] aTouchPositions);

    void touchEnded(TouchPosition[] aTouchPositions);

    void touchMoved(TouchPosition[] aTouchPositions);

    void touchCanceled(TouchPosition[] aTouchPositions);
}
