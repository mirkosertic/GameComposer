package de.mirkosertic.gamecomposer;

import java.io.File;

public class ExportGameHTML5DragomeEvent {

    private final File gameDirectory;

    public ExportGameHTML5DragomeEvent(File aGameDirectory) {
        gameDirectory = aGameDirectory;
    }

    public File getGameDirectory() {
        return gameDirectory;
    }
}