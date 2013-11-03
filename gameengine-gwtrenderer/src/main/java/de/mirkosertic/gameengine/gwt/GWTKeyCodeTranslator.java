package de.mirkosertic.gameengine.gwt;

import com.google.gwt.event.dom.client.KeyCodes;
import de.mirkosertic.gameengine.core.GameKeyCode;

public class GWTKeyCodeTranslator {

    public static GameKeyCode translate(int aNativeCode) {
        switch (aNativeCode) {
            case KeyCodes.KEY_UP:
                return GameKeyCode.UP;
            case KeyCodes.KEY_DOWN:
                return GameKeyCode.DOWN;
            case KeyCodes.KEY_LEFT:
                return GameKeyCode.LEFT;
            case KeyCodes.KEY_RIGHT:
                return GameKeyCode.RIGHT;
        }
        return null;
    }
}
