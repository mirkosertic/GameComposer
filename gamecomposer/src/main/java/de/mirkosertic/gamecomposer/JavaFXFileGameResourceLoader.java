package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.ResourceName;
import de.mirkosertic.gameengine.javafx.JavaFXAbstractGameResourceLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class JavaFXFileGameResourceLoader extends JavaFXAbstractGameResourceLoader {

    private File baseDirectory;

    public JavaFXFileGameResourceLoader(File aBaseDirectory) {
        baseDirectory = aBaseDirectory;
    }

    protected InputStream getInputStreamFor(ResourceName aResourceName) throws IOException {
        return new FileInputStream(new File(baseDirectory, aResourceName.name));
    }
}