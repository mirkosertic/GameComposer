package de.mirkosertic.gameengine.android;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.TouchPosition;

/**
 * Special GestureDetector to emulate mouse events on touch devices.
 */
public class AndroidGestureDetector extends DefaultGestureDetector {

    public AndroidGestureDetector(GameEventManager aEventManager, CameraBehavior aCameraBehavior) {
        super(aEventManager, aCameraBehavior);
    }

    @Override
    public void touchStarted(TouchPosition[] aTouchPositions) {
        super.touchStarted(aTouchPositions);
        // For every finger, fire a mouse event also
        for (TouchPosition thePosition : aTouchPositions) {
            mousePressed(new Position(thePosition.x, thePosition.y));
        }
    }

    @Override
    public void touchEnded(TouchPosition[] aTouchPositions) {
        super.touchEnded(aTouchPositions);
        // For every finger, fire a mouse event also
        for (TouchPosition thePosition : aTouchPositions) {
            mouseReleased(new Position(thePosition.x, thePosition.y));
        }
    }
}
