package de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.action.SetPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class SetPropertyEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SetPropertyEditorController createFor(GameScene aGameScene, SetPropertyAction aAction) {
        try (InputStream fxml = SetPropertyEditorController.class.getResourceAsStream("SetPropertyEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty.SetPropertyEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            SetPropertyEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
