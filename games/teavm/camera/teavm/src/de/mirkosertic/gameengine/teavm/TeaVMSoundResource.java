package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

public class TeaVMSoundResource implements GameResource {

    private final String name;

    public TeaVMSoundResource(String aName) {
        name = aName;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }

    public String getName() {
        return name;
    }
}