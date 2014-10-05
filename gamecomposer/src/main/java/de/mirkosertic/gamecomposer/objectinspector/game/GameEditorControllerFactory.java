package de.mirkosertic.gamecomposer.objectinspector.game;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactory;
import de.mirkosertic.gamecomposer.objectinspector.ObjectInspectorFactoryType;
import de.mirkosertic.gameengine.core.Game;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@Singleton
@ObjectInspectorFactoryType(clazz = Game.class)
public class GameEditorControllerFactory implements ObjectInspectorFactory<Game, GameEditorController> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public GameEditorController create(Game aObject) {
        try (InputStream fxml = GameEditorController.class.getResourceAsStream("GameEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.objectinspector.gameobject.GameObjectEditor");
            theLoader.setResources(theBundle);
            Parent root = theLoader.load(fxml);
            return ((GameEditorController)theLoader.getController()).initialize(root, aObject);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
