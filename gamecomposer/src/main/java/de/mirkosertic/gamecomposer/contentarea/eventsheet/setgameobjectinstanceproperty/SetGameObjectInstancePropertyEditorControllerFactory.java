package de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectinstanceproperty;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.action.SetGameObjectInstancePropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class SetGameObjectInstancePropertyEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SetGameObjectInstancePropertyEditorController createFor(GameScene aGameScene, SetGameObjectInstancePropertyAction aAction) {
        try (InputStream fxml = SetGameObjectInstancePropertyEditorController.class.getResourceAsStream("SetGameObjectInstancePropertyEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectinstanceproperty.SetGameObjectInstancePropertyEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            SetGameObjectInstancePropertyEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
