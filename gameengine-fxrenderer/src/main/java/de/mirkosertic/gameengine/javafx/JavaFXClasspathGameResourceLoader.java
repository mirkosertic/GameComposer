package de.mirkosertic.gameengine.javafx;

import java.io.IOException;
import java.io.InputStream;

import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.resource.GameResource;
import de.mirkosertic.gameengine.resource.GameResourceLoader;

public class JavaFXClasspathGameResourceLoader implements GameResourceLoader {

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        InputStream theStream = getClass().getResourceAsStream(aResourceName.getName().replace('\\', '/'));
        if (theStream == null) {
            return null;
        }
        if (aResourceName.getName().toLowerCase().endsWith(".png")) {
            return new JavaFXBitmapResource(theStream);
        }
        return null;
    }
}
