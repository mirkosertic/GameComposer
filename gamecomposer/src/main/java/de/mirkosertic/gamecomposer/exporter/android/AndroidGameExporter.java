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