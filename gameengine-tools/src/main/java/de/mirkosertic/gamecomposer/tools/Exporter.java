package de.mirkosertic.gamecomposer.tools;

import java.io.File;
import java.io.IOException;

public interface Exporter {

    interface Callback {
        void updateStatus(String aStatusMessage);
    }

    void export(File aGameDirectory, File aTargetDirectory, Callback aStatus) throws IOException;
}
