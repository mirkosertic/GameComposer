package de.mirkosertic.gameengine.core;

import java.io.IOException;

public interface GameResourceLoader {
    
    GameResource load(ResourceName aResourceName) throws IOException;

    void flush();
}