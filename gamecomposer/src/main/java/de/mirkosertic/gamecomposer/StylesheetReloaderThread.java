package de.mirkosertic.gamecomposer;

import javafx.application.Platform;
import javafx.scene.layout.Region;

import java.net.URL;
import java.net.URLConnection;

public class StylesheetReloaderThread extends Thread {

    private final Region region;
    private final URL urlToWatch;

    public StylesheetReloaderThread(Region aRegion, URL aUrlToWatch) {
        region = aRegion;
        urlToWatch = aUrlToWatch;

        setDaemon(true);
        setName("DEV Stylesheetreloader");
    }

    @Override
    public void run() {
        long theLastModified = 0;
        while (!interrupted()) {
            try {
                URLConnection theConnection = urlToWatch.openConnection();
                long theCurrentModified = theConnection.getLastModified();
                if (!(theCurrentModified == theLastModified)) {

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            region.getStylesheets().clear();
                            region.getStylesheets().add(urlToWatch.toExternalForm());
                        }
                    });

                    theLastModified = theCurrentModified;
                }

                Thread.sleep(1000);
            } catch (Exception e) {
                // Should never happen
            }
        }
    }
}
