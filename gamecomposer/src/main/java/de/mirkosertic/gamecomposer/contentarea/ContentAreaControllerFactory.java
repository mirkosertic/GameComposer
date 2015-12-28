package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.ContentArea;
import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

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
            Parent root = theLoader.load(fxml);
            return ((ContentAreaController)theLoader.getController()).initialize(root);
        }
    }
}
