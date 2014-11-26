package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;

public class TeaVMGameResourceLoader implements GameResourceLoader {

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        return null;
    }

    @Override
    public void flush() {
    }
}
