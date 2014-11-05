package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;

public final class Window {

    public Window() {
    }

    public int getClientWidth() {
        return ScriptHelper.evalInt("window.innerWidth", this);
    }

    public int getClientHeight() {
        return ScriptHelper.evalInt("window.innerHeight", this);
    }
}