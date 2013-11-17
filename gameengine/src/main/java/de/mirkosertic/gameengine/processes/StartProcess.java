package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class StartProcess extends GameEvent {

    private final Property<GameProcess> process;

    public StartProcess(GameProcess aProcess) {
        super("StartProcess");
        process = registerProperty(new Property<GameProcess>(this, "process", aProcess));
    }

    public ReadOnlyProperty<GameProcess> processProperty() {
        return process;
    }
}
