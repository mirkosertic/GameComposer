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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class HTML5Exporter implements Exporter {

    private final String resourceName;

    protected HTML5Exporter(String aResourceName) {
        resourceName = aResourceName;
    }

    public void export(File aGameDirectory, File aTargetDirectory, Callback aStatus) throws IOException {
        InputStream theStream = getClass().getResourceAsStream(resourceName);
        ZipInputStream theZipStream = new ZipInputStream(theStream);
        ZipEntry theEntry;
        while ((theEntry = theZipStream.getNextEntry()) != null) {
            if (isValidWARFileEntry(theEntry)) {
                aStatus.updateStatus("Exporting " + theEntry.getName());
                File theTargetFile = new File(aTargetDirectory, theEntry.getName().replace('/', File.separatorChar));
                theTargetFile.getParentFile().mkdirs();
                try (FileOutputStream theFos = new FileOutputStream(theTargetFile)) {
                    IOUtils.copy(theZipStream, theFos);
                }
            }
        }

        aStatus.updateStatus("Copying game assets to " + aGameDirectory);
        FileUtils.copyDirectory(aGameDirectory, aTargetDirectory);

        aStatus.updateStatus("Export finished");
    }

    private boolean isValidWARFileEntry(ZipEntry aEntry) {
        if (aEntry.getName().endsWith("/")) {
            return false;
        }
        if (aEntry.getName().startsWith("WEB-INF")) {
            return false;
        }
        if (aEntry.getName().startsWith("META-INF")) {
            return false;
        }
        if (aEntry.getName().startsWith("scene")) {
            return false;
        }
        if (aEntry.getName().startsWith("game.json")) {
            return false;
        }
        return true;
    }
}
