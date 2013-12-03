package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class SystemTick extends GameEvent {

    private final Property<Long> totalTicks;
    private final Property<Long> gameTime;
    private final Property<Long> elapsedTimeSinceLastLoop;

    public SystemTick(long aTotalTicks, long aGameTime, long aElapsedTimeSinceLastLoop) {
        super("SystemTick");
        totalTicks = registerProperty(new Property<Long>(this, "totalTicks", aTotalTicks));
        gameTime = registerProperty(new Property<Long>(this, "gameTime", aGameTime));
        elapsedTimeSinceLastLoop = registerProperty(new Property<Long>(this, "elapsedTimeSinceLastLoop", aElapsedTimeSinceLastLoop));
    }

    public ReadOnlyProperty<Long> totalTicksProperty() {
        return totalTicks;
    }

    public ReadOnlyProperty<Long> gameTimeProperty() {
        return gameTime;
    }

    public ReadOnlyProperty<Long> elapsedTimeSinceLastLoopProperty() {
        return elapsedTimeSinceLastLoop;
    }
}
