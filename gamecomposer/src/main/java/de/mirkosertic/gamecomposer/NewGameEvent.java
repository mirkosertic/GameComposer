package de.mirkosertic.gamecomposer;

import java.io.File;

public class NewGameEvent {

    private final File projectDirectory;

    public NewGameEvent(File aProjectDirectory) {
        projectDirectory = aProjectDirectory;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }
}