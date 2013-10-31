package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.camera.CameraComponentTemplate;
import de.mirkosertic.gameengine.physics.StaticComponentTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class CameraTemplateEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public CameraTemplateEditorController create(CameraComponentTemplate aObject) {
        try (InputStream fxml = CameraTemplateEditorController.class.getResourceAsStream("CameraTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.cameratemplate.CameraTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((CameraTemplateEditorController) theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
