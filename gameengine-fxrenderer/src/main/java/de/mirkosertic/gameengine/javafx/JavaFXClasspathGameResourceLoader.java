package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.ResourceName;

import java.io.IOException;
import java.io.InputStream;

public class JavaFXClasspathGameResourceLoader extends JavaFXAbstractGameResourceLoader {

    protected InputStream getInputStreamFor(ResourceName aResourceName) throws IOException {
        return getClass().getResourceAsStream(aResourceName.getName().replace('\\', '/'));
    }
}
