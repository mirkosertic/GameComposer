package de.mirkosertic.gamecomposer.exporter.html5;

import de.mirkosertic.gamecomposer.ExportGameHTML5Event;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.StatusEvent;
import org.apache.commons.io.IOUtils;

import javax.enterprise.event.*;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Singleton
public class HTML5GameExporter {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    javax.enterprise.event.Event<StatusEvent> statusEvent;

    private boolean isValidEntry(ZipEntry aEntry) {
        if (aEntry.getName().endsWith("/")) {
            return false;
        }
        if (aEntry.getName().startsWith("WEB-INF")) {
            return false;
        }
        if (aEntry.getName().startsWith("META-INF")) {
            return false;
        }
        if (aEntry.getName().startsWith("scene1")) {
            return false;
        }
        if (aEntry.getName().startsWith("game.json")) {
            return false;
        }
        return true;
    }

    public void onExport(@Observes ExportGameHTML5Event aEvent) throws IOException {
        InputStream theStream = HTML5GameExporter.class.getResourceAsStream("/gameengine-gwtrenderer.war");
        ZipInputStream theZipStream = new ZipInputStream(theStream);
        ZipEntry theEntry;
        while ((theEntry = theZipStream.getNextEntry()) != null) {
            if (isValidEntry(theEntry)) {
                statusEvent.fire(new StatusEvent("Exporting " + theEntry.getName(), StatusEvent.Severity.INFO));
                File theTargetFile = new File(aEvent.getGameDirectory(), theEntry.getName().replace('/', File.separatorChar));
                theTargetFile.getParentFile().mkdirs();
                try (FileOutputStream theFos = new FileOutputStream(theTargetFile)) {
                    IOUtils.copy(theZipStream, theFos);
                }
            }

            statusEvent.fire(new StatusEvent("Copying game assets to " + aEvent.getGameDirectory(), StatusEvent.Severity.INFO));
            persistenceManager.copyGameTo(aEvent.getGameDirectory());
        }

        statusEvent.fire(new StatusEvent("Export finished", StatusEvent.Severity.INFO));

        File theGameIndexFile = new File(aEvent.getGameDirectory(), "index.html");
        Desktop.getDesktop().browse(theGameIndexFile.toURI());
    }
}
