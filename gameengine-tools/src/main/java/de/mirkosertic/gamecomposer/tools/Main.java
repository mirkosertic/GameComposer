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
                LOG.info(aStatusMessage);
            }
        });
    }
}