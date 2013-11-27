package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameView;
import de.mirkosertic.gameengine.types.Size;

public abstract class AbstractWebGameView implements GameView {

    private Size size;

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }
}
