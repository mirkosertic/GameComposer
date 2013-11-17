package de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectproperty;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.action.SetGameObjectPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class SetGameObjectPropertyEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SetGameObjectPropertyEditorController createFor(GameScene aGameScene, SetGameObjectPropertyAction aAction) {
        try (InputStream fxml = SetGameObjectPropertyEditorController.class.getResourceAsStream("SetGameObjectPropertyEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectproperty.SetGameObjectPropertyEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);
            SetGameObjectPropertyEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}