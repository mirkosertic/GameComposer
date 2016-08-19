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
package de.mirkosertic.gamecomposer.exporter.html5;

import de.mirkosertic.gamecomposer.ExportGameHTML5TeaVMEvent;
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
public class HTML5TeaVMGameExporter {

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    javax.enterprise.event.Event<StatusEvent> statusEvent;

    @Inject
    Application application;

    @Inject
    ExporterFactory exporterFactory;

    public void onExport(@Observes ExportGameHTML5TeaVMEvent aEvent) throws IOException {

        Exporter theExporter = exporterFactory.create(ExporterFactory.ExportFormat.TEAVM);
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