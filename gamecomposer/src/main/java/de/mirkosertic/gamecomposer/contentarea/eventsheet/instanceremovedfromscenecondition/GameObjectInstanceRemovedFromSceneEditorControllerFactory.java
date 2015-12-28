package de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceremovedfromscenecondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromSceneCondition;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ConditionEditorType(clazz = GameObjectInstanceRemovedFromSceneCondition.class)
public class GameObjectInstanceRemovedFromSceneEditorControllerFactory implements ConditionControllerFactory<GameObjectInstanceRemovedFromSceneEditorController, GameObjectInstanceRemovedFromSceneCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public GameObjectInstanceRemovedFromSceneEditorController createFor(GameScene aGameScene, GameObjectInstanceRemovedFromSceneCondition aCondition) {
        try (InputStream fxml = GameObjectInstanceRemovedFromSceneEditorController.class.getResourceAsStream("GameObjectInstanceRemovedFromSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceremovedfromscenecondition.GameObjectInstanceRemovedFromSceneEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            GameObjectInstanceRemovedFromSceneEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
