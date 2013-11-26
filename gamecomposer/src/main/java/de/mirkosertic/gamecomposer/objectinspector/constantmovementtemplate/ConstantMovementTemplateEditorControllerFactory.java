package de.mirkosertic.gamecomposer.objectinspector.constantmovementtemplate;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.physics.ConstantMovementTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = ConstantMovementTemplate.class)
public class ConstantMovementTemplateEditorControllerFactory implements ObjectInspectorFactory<ConstantMovementTemplate, ConstantMovementTemplateEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public ConstantMovementTemplateEditorController create(ConstantMovementTemplate aObject) {
        try (InputStream fxml = ConstantMovementTemplateEditorController.class.getResourceAsStream("ConstantMovementTemplateEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.constantmovementtemplate.ConstantMovementTemplateEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((ConstantMovementTemplateEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
