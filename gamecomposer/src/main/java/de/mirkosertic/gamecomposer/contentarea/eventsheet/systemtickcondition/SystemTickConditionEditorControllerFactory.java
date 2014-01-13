package de.mirkosertic.gamecomposer.contentarea.eventsheet.systemtickcondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.action.SystemTickCondition;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ConditionEditorType(clazz = SystemTickCondition.class)
public class SystemTickConditionEditorControllerFactory implements ConditionControllerFactory<SystemTickConditionEditorController, SystemTickCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SystemTickConditionEditorController createFor(GameScene aGameScene, SystemTickCondition aCondition) {
        try (InputStream fxml = SystemTickConditionEditorController.class.getResourceAsStream("SystemTickConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.systemtickcondition.SystemTickConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            SystemTickConditionEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
