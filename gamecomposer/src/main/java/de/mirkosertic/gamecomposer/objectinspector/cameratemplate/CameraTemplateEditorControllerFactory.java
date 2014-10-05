package de.mirkosertic.gamecomposer.objectinspector.cameratemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.camera.Camera;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = Camera.class)
public class CameraTemplateEditorControllerFactory implements ObjectInspectorFactory<Camera, CameraTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public CameraTemplateEditorController create(Camera aObject) {
        try (InputStream fxml = CameraTemplateEditorController.class.getResourceAsStream("CameraTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.cameratemplate.CameraTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((CameraTemplateEditorController) theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
