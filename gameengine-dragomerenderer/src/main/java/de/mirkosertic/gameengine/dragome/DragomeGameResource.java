package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class DragomeGameResource implements GameResource {

    private final GameResourceType type;
    private final String name;

    public DragomeGameResource(String aName, GameResourceType aType) {
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
