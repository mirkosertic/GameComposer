package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.types.Size;

abstract class AbstractWebGameView implements GameView {

    private Size size;

    Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
