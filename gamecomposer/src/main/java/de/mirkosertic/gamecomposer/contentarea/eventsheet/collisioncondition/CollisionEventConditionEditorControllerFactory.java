package de.mirkosertic.gamecomposer.contentarea.eventsheet.collisioncondition;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ConditionEditorType;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.physic.ObjectCollisionCondition;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ConditionEditorType(clazz = ObjectCollisionCondition.class)
public class CollisionEventConditionEditorControllerFactory implements ConditionControllerFactory<CollisionEventConditionEditorController, ObjectCollisionCondition> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public CollisionEventConditionEditorController createFor(GameScene aGameScene, ObjectCollisionCondition aCondition) {
        try (InputStream fxml = CollisionEventConditionEditorController.class.getResourceAsStream("CollisionEventConditionEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.collisioncondition.CollisionEventConditionEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            CollisionEventConditionEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aCondition);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
