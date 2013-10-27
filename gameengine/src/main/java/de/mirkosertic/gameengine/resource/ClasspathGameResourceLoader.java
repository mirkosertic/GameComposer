package de.mirkosertic.gameengine.resource;

import de.mirkosertic.gameengine.core.ResourceName;

import java.io.IOException;
import java.io.InputStream;

public class ClasspathGameResourceLoader implements GameResourceLoader {

    public InputStream getResourceAsStream(ResourceName aResourceName) throws IOException {
        return getClass().getResourceAsStream(aResourceName.getName().replace('\\','/'));
    }
}
