package de.mirkosertic.gameengine.gwt;

import java.io.IOException;

import com.google.gwt.media.client.Audio;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceLoader;
import de.mirkosertic.gameengine.core.ResourceName;

public class GWTGameResourceLoader implements GameResourceLoader {

    static final String holderId = "resourcecache";

    private String baseDirectory;

    public GWTGameResourceLoader(String aBaseDirectory) {
        baseDirectory = aBaseDirectory;
    }

    @Override
    public GameResource load(ResourceName aResourceName) throws IOException {
        if (aResourceName.getName().endsWith(".png")) {
            Image theImage = new Image();
            theImage.setVisible(false);
            theImage.setUrl(baseDirectory + aResourceName.getName().replace('\\', '/'));
            RootPanel.get(holderId).add(theImage);
            return new GWTBitmapResource(theImage);
        }
        if (aResourceName.getName().endsWith(".wav")) {
            Audio theAudio = Audio.createIfSupported();
            theAudio.setSrc(baseDirectory + aResourceName.getName().replace('\\', '/'));
            return new GWTAudioResource(theAudio);
        }

        return null;
    }

    @Override
    public void flush() {
        RootPanel.get(holderId).clear();
    }
}
