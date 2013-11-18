package de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.KeyEventCondition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class KeyEventConditionEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public KeyEventConditionEditorController createFor(GameScene aGameScene, KeyEventCondition aCondition) {
        try (InputStream fxml = KeyEventConditionEditorController.class.getResourceAsStream("KeyEventConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition.KeyEventConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            KeyEventConditionEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
