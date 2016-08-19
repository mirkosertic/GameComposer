/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
