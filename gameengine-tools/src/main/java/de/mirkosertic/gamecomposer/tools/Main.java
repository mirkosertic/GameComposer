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
package de.mirkosertic.gamecomposer.tools;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    public static void main(String aArgs[]) throws IOException {
        LOG.info("Starting export");
        for (int i=0;i<aArgs.length;i++) {
            LOG.log(Level.INFO, " argument {0} : {1}", new Object[] {i, aArgs[i]});
        }

        ExporterFactory.ExportFormat theExportFormat = ExporterFactory.ExportFormat.valueOf(aArgs[0]);
        File theGameDirectory = new File(aArgs[1]);
        File theTargetDirectory = new File(aArgs[2]);

        LOG.log(Level.INFO, "Using format     : {0}", theExportFormat);
        LOG.log(Level.INFO, "Source directory : {0}", theGameDirectory);
        LOG.log(Level.INFO, "Target directory : {0}", theTargetDirectory);

        theTargetDirectory.mkdirs();

        ExporterFactory theFactory = new ExporterFactory();
        Exporter theExporter = theFactory.create(theExportFormat);
        theExporter.export(theGameDirectory, theTargetDirectory, new Exporter.Callback() {
            @Override
            public void updateStatus(String aStatusMessage) {
                LOG.log(Level.FINE, aStatusMessage);
            }
        });
    }
}