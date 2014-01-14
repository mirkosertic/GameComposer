package de.mirkosertic.gamecomposer.objectinspector.constantmovement;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.arcade.ConstantMovement;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@Singleton
@ObjectInspectorFactoryType(clazz = ConstantMovement.class)
public class ConstantMovementEditorFactory implements ObjectInspectorFactory<ConstantMovement, ConstantMovementEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public ConstantMovementEditorController create(ConstantMovement aObject) {
        try (InputStream fxml = ConstantMovementEditorController.class.getResourceAsStream("ConstantMovementEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.constantmovement.ConstantMovementEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((ConstantMovementEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
