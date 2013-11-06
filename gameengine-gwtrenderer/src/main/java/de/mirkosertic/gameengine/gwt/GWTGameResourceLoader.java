package de.mirkosertic.gameengine.gwt;

import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;

import java.io.IOException;

public class GWTGameResourceLoader implements GameResourceLoader {

    static final String holderId = "resourcecache";

    private String baseDirectory;

    public GWTGameResourceLoader(String aBaseDirectory) {
        baseDirectory = aBaseDirectory;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        Image theImage = new Image();
        theImage.setVisible(false);
        theImage.setUrl(baseDirectory + aResourceName.getName().replace('\\', '/'));
        RootPanel.get(holderId).add(theImage);
        return new GWTBitmapResource(theImage);
    }

    @Override
    public void flush() {
        RootPanel.get(holderId).clear();
    }
}