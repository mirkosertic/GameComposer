package de.mirkosertic.gamecomposer;

import java.io.File;

class LoadGameEvent {

    private final File gameFile;

    public LoadGameEvent(File aGameFile) {
        gameFile = aGameFile;
    }

    public File getGameFile() {
        return gameFile;
    }
}
