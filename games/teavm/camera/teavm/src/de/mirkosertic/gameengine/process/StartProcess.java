package de.mirkosertic.gameengine.process;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class StartProcess extends GameEvent {

    private static final StartProcessClassInformation CIINSTANCE = new StartProcessClassInformation();

    @ReflectiveField
    public final GameProcess process;

    public StartProcess(GameProcess aProcess) {
        super("StartProcess");
        process = aProcess;
    }

    @Override
    public StartProcessClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
