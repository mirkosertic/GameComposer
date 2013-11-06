package de.mirkosertic.gamecomposer.objectinspector;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.ObjectInspector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class ObjectInspectorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Produces
    @ObjectInspector
    public ObjectInspectorController create() throws IOException {
        try (InputStream fxml = ObjectInspectorController.class.getResourceAsStream("ObjectInspector.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.ObjectInspector");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((ObjectInspectorController)theLoader.getController()).initialize(root);
        }
    }
}
