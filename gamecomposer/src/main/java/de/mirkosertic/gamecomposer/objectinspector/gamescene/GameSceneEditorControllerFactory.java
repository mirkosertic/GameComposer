package de.mirkosertic.gamecomposer.objectinspector.gamescene;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ObjectInspectorFactoryType(clazz = GameScene.class)
public class GameSceneEditorControllerFactory implements ObjectInspectorFactory<GameScene, GameSceneEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public GameSceneEditorController create(GameScene aObject) {
        try (InputStream fxml = GameSceneEditorController.class.getResourceAsStream("GameSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gamescene.GameSceneEditor");
            theLoader.setResources(theBundle);
            Parent root = (Parent) theLoader.load(fxml);
            return ((GameSceneEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
