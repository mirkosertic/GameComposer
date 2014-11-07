package de.mirkosertic.gamecomposer;

import java.io.File;

public class ExportGameHTML5GWTEvent {

    private final File gameDirectory;

    public ExportGameHTML5GWTEvent(File aGameDirectory) {
        gameDirectory = aGameDirectory;
    }

    public File getGameDirectory() {
        return gameDirectory;
    }
}