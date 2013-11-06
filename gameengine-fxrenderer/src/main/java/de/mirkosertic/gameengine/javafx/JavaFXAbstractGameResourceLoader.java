package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;

import java.io.IOException;
import java.io.InputStream;

public abstract class JavaFXAbstractGameResourceLoader implements GameResourceLoader {

    protected abstract InputStream getInputStreamFor(ResourceName aResourceName) throws IOException;

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        InputStream theStream = getInputStreamFor(aResourceName);
        if (theStream == null) {
            return null;
        }
        if (aResourceName.getName().toLowerCase().endsWith(".png")) {
            return new JavaFXBitmapResource(theStream);
        }
        return null;
    }

    @Override
    public void flush() {
    }
}
