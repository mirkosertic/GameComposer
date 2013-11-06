package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ContentArea;
import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class ContentAreaControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Produces
    @ContentArea
    public ContentAreaController create() throws IOException {
        try (InputStream fxml = ContentAreaController.class.getResourceAsStream("ContentArea.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.ContentArea");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((ContentAreaController)theLoader.getController()).initialize(root);
        }
    }
}
