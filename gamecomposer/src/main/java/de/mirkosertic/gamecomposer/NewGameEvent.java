package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.Game;

import java.io.File;

public class NewGameEvent {

    private final File projectDirectory;
    private final Game game;

    public NewGameEvent(File aProjectDirectory, Game aGame) {
        projectDirectory = aProjectDirectory;
        game = aGame;
    }

    public File getProjectDirectory() {
        return projectDirectory;
    }

    public Game getGame() {
        return game;
    }
}