package de.mirkosertic.gamecomposer;

import java.io.File;

public class ExportGameHTML5TeaVMEvent {

    private final File gameDirectory;

    public ExportGameHTML5TeaVMEvent(File aGameDirectory) {
        gameDirectory = aGameDirectory;
    }

    public File getGameDirectory() {
        return gameDirectory;
    }
}