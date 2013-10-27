package de.mirkosertic.gameengine.resource;

import de.mirkosertic.gameengine.core.ResourceName;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameResourceCache {

    private Map<String, GameResource> cachedResources;
    private GameResourceLoader resourceLoader;
    private Set<GameResourceDecoder> resourceDecoder;

    public GameResourceCache(GameResourceLoader aResourceLoader) {
        cachedResources = new HashMap<String, GameResource>();
        resourceDecoder = new HashSet<GameResourceDecoder>();
        resourceLoader = aResourceLoader;
    }

    public <T extends GameResource> T getResourceFor(ResourceName aResourceName) throws IOException {
        T theResource = (T) cachedResources.get(aResourceName.getName());
        if (theResource == null) {
            InputStream theStreamToResource = resourceLoader.getResourceAsStream(aResourceName);
            if (theStreamToResource == null) {
                throw new IOException("Cannot find resource " + aResourceName.getName());
            }
            for (GameResourceDecoder theDecoder : resourceDecoder) {
                if (theDecoder.canDecode(aResourceName)) {
                    theResource = (T) theDecoder.decodeToResource(theStreamToResource);
                    cachedResources.put(aResourceName.getName(), theResource);
                    return theResource;
                }
            }
            throw new IOException("Cannot find decoder for " + aResourceName.getName());
        }
        return theResource;
    }

    public void addResourceDecoder(GameResourceDecoder aDecoder) {
        resourceDecoder.add(aDecoder);
    }
}
