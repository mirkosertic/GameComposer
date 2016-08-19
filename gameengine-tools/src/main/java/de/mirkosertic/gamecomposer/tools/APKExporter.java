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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.SystemUtils;

public class APKExporter implements Exporter {

    protected APKExporter() {
    }

    @Override
    public void export(File aGameDirectory, File aTargetDirectory, Callback aStatus) throws IOException {
        // Create a new temp directory
        File theTempDirectory = new File(SystemUtils.getJavaIoTmpDir(), "game" + System.currentTimeMillis());

        File theAssetsDirectory = new File(theTempDirectory, "assets");
        theAssetsDirectory.mkdirs();

        aStatus.updateStatus("Copying game assets to " + theAssetsDirectory);

        // And copy the game to the assets directory
        FileUtils.copyDirectory(aGameDirectory, theAssetsDirectory);

        // We need to extract the apk file
        InputStream theStream = getClass().getResourceAsStream("/gameengine-androidrenderer.apk");
        try (ZipInputStream theZipStream = new ZipInputStream(theStream)) {
            ZipEntry theEntry;
            while ((theEntry = theZipStream.getNextEntry()) != null) {
                // Skip all files in META-INF, we do not want the certificate to get copied.
                if (!theEntry.getName().startsWith("META-INF/")) {
                    File theTargetFile = new File(theTempDirectory, theEntry.getName().replace('/', File.separatorChar));
                    theTargetFile.getParentFile().mkdirs();

                    aStatus.updateStatus("Copying " + theEntry.getName());

                    try (FileOutputStream theFos = new FileOutputStream(theTargetFile)) {
                        IOUtils.copy(theZipStream, theFos);
                    }
                }
            }
        }

        String theBaseName = theTempDirectory.toString();
        File theGameFile = new File(aTargetDirectory, "game.apk");
        try (ZipOutputStream theZipStream = new ZipOutputStream(new FileOutputStream(theGameFile))) {
            for (File theFile : FileUtils
                    .listFilesAndDirs(theTempDirectory, TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY)) {
                String theRelativeName = theFile.toString().substring(theBaseName.length()).replace(File.separatorChar, '/');
                if (theRelativeName.length() > 0) {
                    theRelativeName = theRelativeName.substring(1);
                    if (theFile.isDirectory()) {
                        ZipEntry theEntry = new ZipEntry(theRelativeName + "/");
                        theEntry.setTime(theFile.lastModified());
                        theZipStream.putNextEntry(theEntry);
                        theZipStream.closeEntry();
                    } else {

                        aStatus.updateStatus("Zipping " + theRelativeName);

                        ZipEntry theEntry = new ZipEntry(theRelativeName);
                        theEntry.setTime(theFile.lastModified());
                        theZipStream.putNextEntry(theEntry);
                        try (FileInputStream theFis = new FileInputStream(theFile)) {
                            IOUtils.copy(theFis, theZipStream);
                        }
                        theZipStream.closeEntry();
                    }
                }
            }
        }

        aStatus.updateStatus("Deleting temp directory");

        FileUtils.deleteDirectory(theTempDirectory);

        String theJavaHome = System.getenv("JAVA_HOME");

        if (theJavaHome != null) {
            File theJavaHomeFile = new File(theJavaHome);
            File theJavaBinFile = new File(theJavaHomeFile, "bin");
            File theZipSigner;
            if (SystemUtils.IS_OS_WINDOWS) {
                theZipSigner = new File(theJavaBinFile, "jarsigner.exe");
            } else {
                theZipSigner = new File(theJavaBinFile, "jarsigner");
            }
            if (theZipSigner.exists()) {

                File theUserHome = SystemUtils.getUserHome();
                File theAndroidHome = new File(theUserHome, ".android");
                File theAndroidDebugKeyStore = new File(theAndroidHome, "debug.keystore");

                System.out.println(theZipSigner + " -keystore " + theAndroidDebugKeyStore + " -storepass android -keypass android " + theGameFile.toString() + " androiddebugkey");

                Process theProcess = Runtime.getRuntime().exec(new String[]{theZipSigner.toString(),
                        "-sigFile",
                        "CERT",
                        "-digestalg",
                        "SHA1",
                        "-sigalg",
                        "MD5withRSA",
                        "-keystore",
                        theAndroidDebugKeyStore.toString(),
                        "storepass",
                        "android",
                        "-keypass",
                        "android",
                        theGameFile.toString(),
                        "androiddebugkey"
                });
                try {
                    int theResult = theProcess.waitFor();

                    aStatus.updateStatus("Finished signing of APK with Android Debug Key, Result = " + theResult);

                } catch (InterruptedException e) {
                    aStatus.updateStatus("Cannot execute ZipSigner : " + e.getMessage());
                    throw new IOException(e);
                }
            } else {
                aStatus.updateStatus("Cannot execute ZipSigner");
            }
        }
    }
}