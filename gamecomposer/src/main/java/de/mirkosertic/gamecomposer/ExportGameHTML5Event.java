package de.mirkosertic.gamecomposer;

import java.io.File;

public class ExportGameHTML5Event {

    private final File gameDirectory;

    public ExportGameHTML5Event(File aGameDirectory) {
        gameDirectory = aGameDirectory;
    }

    public File getGameDirectory() {
        return gameDirectory;
    }
}