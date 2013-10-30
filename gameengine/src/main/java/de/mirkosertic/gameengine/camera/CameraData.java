package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.Size;

public class CameraData {

    private Size screenSize;

    protected CameraData() {
        screenSize = new Size(640, 480);
    }

    public Size getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Size screenSize) {
        this.screenSize = screenSize;
    }
}
