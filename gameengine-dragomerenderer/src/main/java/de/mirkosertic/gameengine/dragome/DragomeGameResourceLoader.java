package de.mirkosertic.gameengine.dragome;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;

public class DragomeGameResourceLoader implements GameResourceLoader {

    private final String sceneId;

    public DragomeGameResourceLoader(String aSceneId) {
        sceneId = aSceneId;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        String theResourceName = sceneId + aResourceName.name;
        return new DragomeGameResource(theResourceName, GameResourceType.BITMAP);
    }

    @Override
    public void flush() {
    }
}