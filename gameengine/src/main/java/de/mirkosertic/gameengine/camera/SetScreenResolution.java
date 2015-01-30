package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;
import de.mirkosertic.gameengine.type.Size;

@InheritedClassInformation
public class SetScreenResolution extends GameEvent {

    private static final SetScreenResolutionClassInformation CIINSTANCE = new SetScreenResolutionClassInformation();

    @ReflectiveField
    public final Size screenSize;

    public SetScreenResolution(Size aSize) {
        super("SetScreenResolution");
        screenSize = aSize;
    }

    @Override
    public SetScreenResolutionClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}