package de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.KeyEventCondition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ConditionEditorType(clazz = KeyEventCondition.class)
public class KeyEventConditionEditorControllerFactory implements ConditionControllerFactory<KeyEventConditionEditorController, KeyEventCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public KeyEventConditionEditorController createFor(GameScene aGameScene, KeyEventCondition aCondition) {
        try (InputStream fxml = KeyEventConditionEditorController.class.getResourceAsStream("KeyEventConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.keyeventcondition.KeyEventConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            KeyEventConditionEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
