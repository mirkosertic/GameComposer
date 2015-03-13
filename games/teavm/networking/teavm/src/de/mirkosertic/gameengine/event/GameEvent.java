package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.type.Reflectable;

public abstract class GameEvent implements Reflectable<GameEventClassInformation> {

    private static final GameEventClassInformation CIINSTANCE = new GameEventClassInformation();

    @ReflectiveField
    public final String type;

    protected GameEvent(String aType) {
        type = aType;
    }

    @Override
    public GameEventClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}