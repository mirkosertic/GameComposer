package de.mirkosertic.gameengine.resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.mirkosertic.gameengine.core.ResourceName;

public class GameResourceCache {

    private Map<String, GameResource> cachedResources;
    private GameResourceLoader resourceLoader;

    public GameResourceCache(GameResourceLoader aResourceLoader) {
        cachedResources = new HashMap<String, GameResource>();
        resourceLoader = aResourceLoader;
    }

    public <T extends GameResource> T getResourceFor(ResourceName aResourceName) throws IOException {
        T theResource = (T) cachedResources.get(aResourceName.getName());
        if (theResource == null) {
            theResource = (T) resourceLoader.load(aResourceName);
            if (theResource == null) {
                throw new IOException("Cannot load resource " + aResourceName.getName());
            }
            cachedResources.put(aResourceName.getName(), theResource);
            return theResource;
        }
        return theResource;
    }

    public void flush() {
        resourceLoader.flush();
        cachedResources.clear();
    }
}
