package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.resource.GameResourceDecoder;

import java.io.InputStream;

public class JavaFXBitmapDecoder implements GameResourceDecoder<JavaFXBitmapResource> {

    public boolean canDecode(ResourceName aResourceName) {
        return aResourceName.getName().toLowerCase().endsWith(".png");
    }

    public JavaFXBitmapResource decodeToResource(InputStream aInputStream) {
        return new JavaFXBitmapResource(aInputStream);
    }
}
