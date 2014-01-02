package de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceleftlayout;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameObjectInstanceLeftLayoutCondition;
import de.mirkosertic.gameengine.core.GameScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ConditionEditorType(clazz = GameObjectInstanceLeftLayoutCondition.class)
public class GameObjectInstanceLeftLayoutEditorControllerFactory implements ConditionControllerFactory<GameObjectInstanceLeftLayoutEditorController, GameObjectInstanceLeftLayoutCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public GameObjectInstanceLeftLayoutEditorController createFor(GameScene aGameScene, GameObjectInstanceLeftLayoutCondition aCondition) {
        try (InputStream fxml = GameObjectInstanceLeftLayoutEditorController.class.getResourceAsStream("GameObjectInstanceLeftLayoutEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.instanceleftlayoutcondition.GameObjectInstanceLeftLayoutEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            GameObjectInstanceLeftLayoutEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
