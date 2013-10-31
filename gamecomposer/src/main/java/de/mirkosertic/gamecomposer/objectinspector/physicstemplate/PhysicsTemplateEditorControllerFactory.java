package de.mirkosertic.gamecomposer.objectinspector.physicstemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.physics.PhysicsComponentTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class PhysicsTemplateEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public PhysicsTemplateEditorController create(PhysicsComponentTemplate aObject) {
        try (InputStream fxml = PhysicsTemplateEditorController.class.getResourceAsStream("PhysicsTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.physicstemplate.PhysicsTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((PhysicsTemplateEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
