package de.mirkosertic.gamecomposer.objectinspector.gameobject;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.core.GameObject;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameObjectEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public GameObjectEditorController create(GameObject aObject) {
        try (InputStream fxml = GameObjectEditorController.class.getResourceAsStream("GameObjectEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gameobject.GameObjectEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((GameObjectEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
