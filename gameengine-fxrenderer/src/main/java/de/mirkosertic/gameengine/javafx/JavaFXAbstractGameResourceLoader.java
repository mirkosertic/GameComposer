package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.type.ResourceName;

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
        if (aResourceName.name.toLowerCase().endsWith(".png")) {
            return new JavaFXBitmapResource(theStream);
        }
        try {
            if (aResourceName.name.toLowerCase().endsWith(".au")) {
                return new JavaFXAudioResource(theStream);
            }
            if (aResourceName.name.toLowerCase().endsWith(".wav")) {
                return new JavaFXAudioResource(theStream);
            }
        } catch (IOException e) {
            throw e;
        } catch (Exception e) {
            throw new IOException(e);
        }
        return null;
    }

    @Override
    public void flush() {
    }
}
