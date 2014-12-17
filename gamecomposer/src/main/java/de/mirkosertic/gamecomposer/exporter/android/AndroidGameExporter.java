package de.mirkosertic.gamecomposer.exporter.android;

import de.mirkosertic.gamecomposer.ExportGameAndroidEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.StatusEvent;
import javafx.application.Application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.lang3.SystemUtils;

@Singleton
public class AndroidGameExporter {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<StatusEvent> statusEvent;

    @Inject
    Application application;

    public void onExport(@Observes ExportGameAndroidEvent aEvent) throws IOException {

        // Create a new temp directory
        File theTempDirectory = new File(SystemUtils.getJavaIoTmpDir(), "game" + System.currentTimeMillis());

        File theAssetsDirectory = new File(theTempDirectory, "assets");
        theAssetsDirectory.mkdirs();

        statusEvent.fire(new StatusEvent("Copying game assets to " + theAssetsDirectory, StatusEvent.Severity.INFO));

        // And copy the game to the assets directory
        persistenceManager.copyGameTo(theAssetsDirectory);

        // We need to extract the apk file
        InputStream theStream = AndroidGameExporter.class.getResourceAsStream("/gameengine-androidrenderer.apk");
        try (ZipInputStream theZipStream = new ZipInputStream(theStream)) {
            ZipEntry theEntry;
            while ((theEntry = theZipStream.getNextEntry()) != null) {
                // Skip all files in META-INF, we do not want the certificate to get copied.
                if (!theEntry.getName().startsWith("META-INF/")) {
                    File theTargetFile = new File(theTempDirectory, theEntry.getName().replace('/', File.separatorChar));
                    theTargetFile.getParentFile().mkdirs();

                    statusEvent.fire(new StatusEvent("Copying " + theEntry.getName(), StatusEvent.Severity.INFO));

                    try (FileOutputStream theFos = new FileOutputStream(theTargetFile)) {
                        IOUtils.copy(theZipStream, theFos);
                    }
                }
            }
        }

        String theBaseName = theTempDirectory.toString();
        File theGameFile = new File(aEvent.getGameDirectory(), "game.apk");
        try (ZipOutputStream theZipStream = new ZipOutputStream(new FileOutputStream(theGameFile))) {
            for (File theFile : FileUtils.listFilesAndDirs(theTempDirectory, TrueFileFilter.INSTANCE, DirectoryFileFilter.DIRECTORY)) {
                String theRelativeName = theFile.toString().substring(theBaseName.length()).replace(File.separatorChar, '/');
                if (theRelativeName.length() > 0) {
                    theRelativeName = theRelativeName.substring(1);
                    if (theFile.isDirectory()) {
                        ZipEntry theEntry = new ZipEntry(theRelativeName + "/");
                        theEntry.setTime(theFile.lastModified());
                        theZipStream.putNextEntry(theEntry);
                        theZipStream.closeEntry();
                    } else {

                        statusEvent.fire(new StatusEvent("Zipping " + theRelativeName, StatusEvent.Severity.INFO));

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

        statusEvent.fire(new StatusEvent("Deleting temp directory", StatusEvent.Severity.INFO));

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

                    statusEvent.fire(new StatusEvent("Finished signing of APK with Android Debug Key, Result = " + theResult, StatusEvent.Severity.ERROR));

                } catch (InterruptedException e) {
                    statusEvent.fire(new StatusEvent("Cannot execute ZipSigner : " + e.getMessage(), StatusEvent.Severity.ERROR));
                    throw new IOException(e);
                }
            } else {
                statusEvent.fire(new StatusEvent("Cannot execute ZipSigner", StatusEvent.Severity.ERROR));
            }

        }

        application.getHostServices().showDocument(aEvent.getGameDirectory().toString());
    }
}
