package de.mirkosertic.gamecomposer.objectinspector.playerscore;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.playerscore.PlayerScore;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = PlayerScore.class)
public class PlayerScoreEditorControllerFactory implements ObjectInspectorFactory<PlayerScore, PlayerScoreEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public PlayerScoreEditorController create(PlayerScore aObject) {
        try (InputStream fxml = PlayerScoreEditorController.class.getResourceAsStream("PlayerScoreEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.playerscore.PlayerScoreEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((PlayerScoreEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
