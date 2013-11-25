package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class GameLoopRun extends GameEvent {

    private final Property<Long> gameTime;
    private final Property<Long> elapsedTimeSinceLastLoop;

    public GameLoopRun(long aGameTime, long aElapsedTimeSinceLastLoop) {
        super("GameLoopRun");

        gameTime = registerProperty(new Property<Long>(this, "gameTime", aGameTime));
        elapsedTimeSinceLastLoop = registerProperty(new Property<Long>(this, "elapsedTimeSinceLastLoop", aElapsedTimeSinceLastLoop));
    }

    public ReadOnlyProperty<Long> gameTimeProperty() {
        return gameTime;
    }

    public ReadOnlyProperty<Long> elapsedTimeSinceLastLoopProperty() {
        return elapsedTimeSinceLastLoop;
    }
}
