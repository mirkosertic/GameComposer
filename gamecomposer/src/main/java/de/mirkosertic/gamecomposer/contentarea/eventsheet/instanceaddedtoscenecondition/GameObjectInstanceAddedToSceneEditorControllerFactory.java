package de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceaddedtoscenecondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToSceneCondition;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ConditionEditorType(clazz = GameObjectInstanceAddedToSceneCondition.class)
public class GameObjectInstanceAddedToSceneEditorControllerFactory implements ConditionControllerFactory<GameObjectInstanceAddedToSceneEditorController, GameObjectInstanceAddedToSceneCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public GameObjectInstanceAddedToSceneEditorController createFor(GameScene aGameScene, GameObjectInstanceAddedToSceneCondition aCondition) {
        try (InputStream fxml = GameObjectInstanceAddedToSceneEditorController.class.getResourceAsStream("GameObjectInstanceAddedToSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceaddedtoscenecondition.GameObjectInstanceAddedToSceneEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            GameObjectInstanceAddedToSceneEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
