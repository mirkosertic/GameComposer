package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.resource.GameResource;
import de.mirkosertic.gameengine.resource.GameResourceType;
import javafx.scene.image.Image;

import java.io.InputStream;

public class JavaFXBitmapResource extends Image implements GameResource {

    JavaFXBitmapResource(InputStream inputStream) {
        super(inputStream);
    }

    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }
}
