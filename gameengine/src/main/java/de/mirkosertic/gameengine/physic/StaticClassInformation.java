package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.ClassInformation;

public class StaticClassInformation extends ClassInformation {

    public static final StaticClassInformation INSTANCE = new StaticClassInformation();

    private StaticClassInformation() {
    }
}
