package de.mirkosertic.gameengine.resource;

import java.io.IOException;

import de.mirkosertic.gameengine.core.ResourceName;

public interface GameResourceLoader {
    
    GameResource load(ResourceName aResourceName) throws IOException;

    void flush();
}