package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.FXMLLoaderProducer;
import de.mirkosertic.gameengine.core.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class GameEditorControllerFactory {

    @Inject
    FXMLLoaderProducer fxmlLoaderProducer;

    public GameEditorController create(Game aObject) {
        try (InputStream fxml = GameEditorController.class.getResourceAsStream("GameEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderProducer.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gameobject.GameObjectEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((GameEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
