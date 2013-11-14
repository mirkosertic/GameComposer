package de.mirkosertic.gameengine.gwt;

import java.io.IOException;

import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.types.ResourceName;

public class GWTGameResourceLoader implements GameResourceLoader {

    private static final String holderId = "resourcecache";

    private final String baseDirectory;

    public GWTGameResourceLoader(String aBaseDirectory) {
        baseDirectory = aBaseDirectory;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        if (aResourceName.name.endsWith(".png")) {
            Image theImage = new Image();
            theImage.setVisible(false);
            theImage.setUrl(baseDirectory + aResourceName.name.replace('\\', '/'));
            RootPanel.get(holderId).add(theImage);
            return new GWTBitmapResource(theImage);
        }
        if (aResourceName.name.endsWith(".wav")) {
            Audio theAudio = Audio.createIfSupported();
            theAudio.setSrc(baseDirectory + aResourceName.name.replace('\\', '/'));
            return new GWTAudioResource(theAudio);
        }

        return null;
    }

    @Override
    public void flush() {
        RootPanel.get(holderId).clear();
    }
}
