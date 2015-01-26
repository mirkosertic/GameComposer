package de.mirkosertic.gamecomposer.contentarea.eventsheet.mousecondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.input.MouseEventCondition;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ConditionEditorType(clazz = MouseEventCondition.class)
public class MouseEventConditionControllerFactory implements
        ConditionControllerFactory<MouseEventConditionController, MouseEventCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public MouseEventConditionController createFor(GameScene aGameScene, MouseEventCondition aCondition) {
        try (InputStream fxml = MouseEventConditionController.class.getResourceAsStream("MouseEventConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.mousecondition.MouseEventConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            MouseEventConditionController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
