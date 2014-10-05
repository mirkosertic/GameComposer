package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = GameObjectInstance.class)
public class GameObjectInstanceEditorControllerFactory implements ObjectInspectorFactory<GameObjectInstance, GameObjectInstanceEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public GameObjectInstanceEditorController create(GameObjectInstance aObject) {
        try (InputStream fxml = GameObjectInstanceEditorController.class.getResourceAsStream("GameObjectInstanceEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance.GameObjectInstanceEditor");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((GameObjectInstanceEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
