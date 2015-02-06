package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class SystemTick extends GameEvent {

    private static final SystemTickClassInformation CIINSTANCE = new SystemTickClassInformation();

    @ReflectiveField
    public final long totalTicks;

    @ReflectiveField
    public final long gameTime;

    @ReflectiveField
    public final long elapsedTimeSinceLastLoop;

    SystemTick(long aTotalTicks, long aGameTime, long aElapsedTimeSinceLastLoop) {
        super("SystemTick");
        totalTicks = aTotalTicks;
        gameTime = aGameTime;
        elapsedTimeSinceLastLoop = aElapsedTimeSinceLastLoop;
    }

    @Override
    public SystemTickClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
