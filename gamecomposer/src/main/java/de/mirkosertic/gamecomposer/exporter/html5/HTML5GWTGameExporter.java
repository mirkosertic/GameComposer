package de.mirkosertic.gamecomposer.exporter.html5;

import de.mirkosertic.gamecomposer.ExportGameHTML5GWTEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.StatusEvent;
import de.mirkosertic.gamecomposer.tools.Exporter;
import de.mirkosertic.gamecomposer.tools.ExporterFactory;
import javafx.application.Application;

import java.io.File;
import java.io.IOException;
import javax.enterprise.context.Dependent;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@Dependent
public class HTML5GWTGameExporter {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    javax.enterprise.event.Event<StatusEvent> statusEvent;

    @Inject
    Application application;

    @Inject
    ExporterFactory exporterFactory;

    public void onExport(@Observes ExportGameHTML5GWTEvent aEvent) throws IOException {

        Exporter theExporter = exporterFactory.create(ExporterFactory.ExportFormat.GWT);
        theExporter.export(persistenceManager.getCurrentGameDirectory(), aEvent.getGameDirectory(), new Exporter.Callback() {
            @Override
            public void updateStatus(String aStatusMessage) {
                statusEvent.fire(new StatusEvent(aStatusMessage, StatusEvent.Severity.INFO));
            }
        });

        File theGameIndexFile = new File(aEvent.getGameDirectory(), "index.html");
        application.getHostServices().showDocument(theGameIndexFile.toString());
    }
}
