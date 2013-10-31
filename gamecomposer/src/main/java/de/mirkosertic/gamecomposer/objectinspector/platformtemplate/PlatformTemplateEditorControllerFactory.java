package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class PlatformTemplateEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public PlatformTemplateEditorController create(PlatformComponentTemplate aObject) {
        try (InputStream fxml = PlatformTemplateEditorController.class.getResourceAsStream("PlatformTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.platformtemplate.PlatformTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((PlatformTemplateEditorController) theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
