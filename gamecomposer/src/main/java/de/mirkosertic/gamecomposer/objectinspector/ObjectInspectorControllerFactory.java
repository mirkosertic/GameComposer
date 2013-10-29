package de.mirkosertic.gamecomposer.objectinspector;

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
    FXMLLoader fxmlLoader;

    @Produces
    @ObjectInspector
    public ObjectInspectorController create() throws IOException {
        try (InputStream fxml = ObjectInspectorController.class.getResourceAsStream("ObjectInspector.fxml")) {
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.ObjectInspector");
            fxmlLoader.setResources(theBundle);
            Parent root = (Parent) fxmlLoader.load(fxml);
            return ((ObjectInspectorController)fxmlLoader.getController()).initialize(root);
        }
    }
}
