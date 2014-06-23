package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GameResourceCache {

    private final Map<String, GameResource> cachedResources;
    private final GameResourceLoader resourceLoader;

    public GameResourceCache(GameResourceLoader aResourceLoader) {
        cachedResources = new HashMap<>();
        resourceLoader = aResourceLoader;
    }

    public <T extends GameResource> T getResourceFor(ResourceName aResourceName) throws IOException {
        T theResource = (T) cachedResources.get(aResourceName.name);
        if (theResource == null) {
            theResource = (T) resourceLoader.load(aResourceName);
            if (theResource == null) {
                throw new IOException("Cannot load resource " + aResourceName.name);
            }
            cachedResources.put(aResourceName.name, theResource);
            return theResource;
        }
        return theResource;
    }

    public void flush() {
        resourceLoader.flush();
        cachedResources.clear();
    }
}
