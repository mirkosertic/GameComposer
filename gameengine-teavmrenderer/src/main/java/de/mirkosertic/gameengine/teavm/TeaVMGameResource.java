package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class TeaVMGameResource implements GameResource {

    private final GameResourceType type;
    private final String name;

    public TeaVMGameResource(String aName, GameResourceType aType) {
        name = aName;
        type = aType;
    }

    @Override
    public GameResourceType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
