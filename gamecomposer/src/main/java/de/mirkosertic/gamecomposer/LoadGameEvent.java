package de.mirkosertic.gamecomposer;

import java.io.File;

public class LoadGameEvent {

    private File gameFile;

    public LoadGameEvent(File aGameFile) {
        gameFile = aGameFile;
    }

    public File getGameFile() {
        return gameFile;
    }
}
