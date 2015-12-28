package de.mirkosertic.gameengine.gwt;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;

import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.user.client.ui.Image;

public class GWTBitmapResource implements GameResource {

    private final Image image;
    private boolean loaded;
    private ImageElement imageElement;

    public GWTBitmapResource(Image aImage) {
        image = aImage;
        aImage.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent loadEvent) {
                imageElement = ImageElement.as(image.getElement());
                loaded = true;
            }
        });
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.BITMAP;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public ImageElement getImage() {
        return imageElement;
    }
}
