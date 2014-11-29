package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;

public class TeaVMGameResourceLoader implements GameResourceLoader {

    private final String sceneId;

    public TeaVMGameResourceLoader(String aSceneID) {
        sceneId = aSceneID;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        String theResourceName = sceneId + aResourceName.name;
        if (aResourceName.name.endsWith(".wav")) {
            return new TeaVMGameResource(theResourceName, GameResourceType.SOUND);
        }
        return new TeaVMGameResource(theResourceName, GameResourceType.BITMAP);
    }

    @Override
    public void flush() {
    }
}
