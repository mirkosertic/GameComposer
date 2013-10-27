package de.mirkosertic.gameengine.resource;

import de.mirkosertic.gameengine.core.ResourceName;

import java.io.IOException;
import java.io.InputStream;

public interface GameResourceLoader {

    InputStream getResourceAsStream(ResourceName aResourceName) throws IOException;
}
