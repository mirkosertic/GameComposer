package de.mirkosertic.gamecomposer;

import java.io.File;

public class ExportGameAndroidEvent {

    private final File gameDirectory;

    public ExportGameAndroidEvent(File aGameDirectory) {
        gameDirectory = aGameDirectory;
    }

    public File getGameDirectory() {
        return gameDirectory;
    }
}