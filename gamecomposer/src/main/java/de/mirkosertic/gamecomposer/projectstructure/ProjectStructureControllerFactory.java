package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.ProjectStructure;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class ProjectStructureControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Produces
    @ProjectStructure
    public ProjectStructureController create() throws IOException {
        try (InputStream fxml = ProjectStructureController.class.getResourceAsStream("ProjectStructure.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.projectstructure.ProjectStructure");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((ProjectStructureController)theLoader.getController()).initialize(root);
        }
    }

}
