package de.mirkosertic.gamecomposer.contentarea.eventsheet.scenestarted;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.SceneStartedCondition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ConditionEditorType(clazz = SceneStartedCondition.class)
public class SceneStartedConditionEditorControllerFactory implements ConditionControllerFactory<SceneStartedConditionEditorController, SceneStartedCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public SceneStartedConditionEditorController createFor(GameScene aGameScene, SceneStartedCondition aCondition) {
        try (InputStream fxml = SceneStartedConditionEditorController.class.getResourceAsStream("SceneStartedConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.scenestarted.SceneStartedConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            SceneStartedConditionEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}