package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;

public interface GameResourceLoader {
    
    GameResource load(ResourceName aResourceName) throws IOException;

    void flush();
}