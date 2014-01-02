package de.mirkosertic.gameengine.gwt;

import com.google.gwt.event.dom.client.KeyCodes;

import de.mirkosertic.gameengine.type.GameKeyCode;

class GWTKeyCodeTranslator {

    public static GameKeyCode translate(int aNativeCode) {
        switch (aNativeCode) {
            case KeyCodes.KEY_TAB:
                return GameKeyCode.TAB;
            case KeyCodes.KEY_SHIFT:
                return GameKeyCode.SHIFT;
            case KeyCodes.KEY_PAGEUP:
                return GameKeyCode.PAGE_UP;
            case KeyCodes.KEY_PAGEDOWN:
                return GameKeyCode.PAGE_DOWN;
            case KeyCodes.KEY_HOME:
                return GameKeyCode.HOME;
            case KeyCodes.KEY_ESCAPE:
                return GameKeyCode.ESCAPE;
            case KeyCodes.KEY_ENTER:
                return GameKeyCode.ENTER;
            case KeyCodes.KEY_END:
                return GameKeyCode.END;
            case KeyCodes.KEY_DELETE:
                return GameKeyCode.DELETE;
            case KeyCodes.KEY_CTRL:
                return GameKeyCode.CONTROL;
            case KeyCodes.KEY_ALT:
                return GameKeyCode.ALT;
            case KeyCodes.KEY_BACKSPACE:
                return GameKeyCode.BACK_SPACE;
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
