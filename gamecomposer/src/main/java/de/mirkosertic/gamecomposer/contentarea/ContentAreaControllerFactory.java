package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ContentArea;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class ContentAreaControllerFactory {

    @Inject
    FXMLLoader fxmlLoader;

    @Produces
    @ContentArea
    public ContentAreaController create() throws IOException {
        try (InputStream fxml = ContentAreaController.class.getResourceAsStream("ContentArea.fxml")) {
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.ContentArea");
            fxmlLoader.setResources(theBundle);
            Parent root = (Parent) fxmlLoader.load(fxml);
            return ((ContentAreaController)fxmlLoader.getController()).initialize(root);
        }
    }
}
