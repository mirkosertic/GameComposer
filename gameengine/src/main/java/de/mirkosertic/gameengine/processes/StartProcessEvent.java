package de.mirkosertic.gameengine.processes;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.ReadOnlyProperty;

public class StartProcessEvent extends GameEvent {

    private ReadOnlyProperty<GameProcess> process;

    public StartProcessEvent(GameProcess aProcess) {
        super("StartProcessEvent");
        process = registerProperty(new ReadOnlyProperty<GameProcess>(this, "process", aProcess));
    }

    public ReadOnlyProperty<GameProcess> processProperty() {
        return process;
    }
}
