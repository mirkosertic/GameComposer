package de.mirkosertic.gamecomposer.projectstructure;

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
    FXMLLoader fxmlLoader;

    @Produces
    @ProjectStructure
    public ProjectStructureController create() throws IOException {
        try (InputStream fxml = ProjectStructureController.class.getResourceAsStream("ProjectStructure.fxml")) {
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.projectstructure.ProjectStructure");
            fxmlLoader.setResources(theBundle);
            Parent root = (Parent) fxmlLoader.load(fxml);
            return ((ProjectStructureController)fxmlLoader.getController()).initialize(root);
        }
    }

}
