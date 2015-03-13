package de.mirkosertic.gamecomposer.exporter.android;

import de.mirkosertic.gamecomposer.ExportGameAndroidEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.StatusEvent;
import de.mirkosertic.gamecomposer.tools.Exporter;
import de.mirkosertic.gamecomposer.tools.ExporterFactory;
import javafx.application.Application;

import java.io.IOException;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class AndroidGameExporter {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<StatusEvent> statusEvent;

    @Inject
    Application application;

    @Inject
    ExporterFactory exporterFactory;

    public void onExport(@Observes ExportGameAndroidEvent aEvent) throws IOException {

        Exporter theExporter = exporterFactory.create(ExporterFactory.ExportFormat.APK);
        theExporter.export(persistenceManager.getCurrentGameDirectory(), aEvent.getGameDirectory(), new Exporter.Callback() {
            @Override
            public void updateStatus(String aStatusMessage) {
                statusEvent.fire(new StatusEvent(aStatusMessage, StatusEvent.Severity.INFO));
            }
        });

        application.getHostServices().showDocument(aEvent.getGameDirectory().toString());
    }
}