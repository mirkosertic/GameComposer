package de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.sprites.SpriteComponentTemplate;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameObjectInstanceEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public GameObjectInstanceEditorController create(GameObjectInstance aObject) {
        try (InputStream fxml = GameObjectInstanceEditorController.class.getResourceAsStream("GameObjectInstanceEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gameobjectinstance.GameObjectInstanceEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((GameObjectInstanceEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
