package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscene;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.RunSceneAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ActionEditorType(clazz = RunSceneAction.class)
public class RunSceneEditorControllerFactory implements ActionControllerFactory<RunSceneEditorController, RunSceneAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public RunSceneEditorController createFor(GameScene aGameScene, GameRule aRule, RunSceneAction aAction) {
        try (InputStream fxml = RunSceneEditorController.class.getResourceAsStream("RunSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.runscene.RunSceneEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            RunSceneEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
