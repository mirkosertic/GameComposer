package de.mirkosertic.gamecomposer.contentarea.eventsheet.spawninstance;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.SpawnGameObjectInstanceAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ActionEditorType(clazz = SpawnGameObjectInstanceAction.class)
public class SpawnInstanceEditorControllerFactory implements ActionControllerFactory<SpawnIstanceEditorController, SpawnGameObjectInstanceAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public SpawnIstanceEditorController createFor(GameScene aGameScene, GameRule aRule, SpawnGameObjectInstanceAction aAction) {
        try (InputStream fxml = SpawnIstanceEditorController.class.getResourceAsStream("SpawnInstanceEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.spawninstance.SpawnInstanceEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            SpawnIstanceEditorController theController = theLoader.getController();
            return theController.initialize(aGameScene, theRoot, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
