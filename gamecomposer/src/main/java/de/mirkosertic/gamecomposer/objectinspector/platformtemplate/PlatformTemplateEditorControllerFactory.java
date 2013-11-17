package de.mirkosertic.gamecomposer.objectinspector.platformtemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.physics.PlatformComponentTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = PlatformComponentTemplate.class)
public class PlatformTemplateEditorControllerFactory implements ObjectInspectorFactory<PlatformComponentTemplate, PlatformTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public PlatformTemplateEditorController create(PlatformComponentTemplate aObject) {
        try (InputStream fxml = PlatformTemplateEditorController.class.getResourceAsStream("PlatformTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.platformtemplate.PlatformTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((PlatformTemplateEditorController) theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
