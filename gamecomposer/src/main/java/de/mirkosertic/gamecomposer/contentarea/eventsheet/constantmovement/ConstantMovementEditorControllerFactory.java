package de.mirkosertic.gamecomposer.contentarea.eventsheet.constantmovement;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance.DeleteInstanceEditorController;
import de.mirkosertic.gameengine.arcade.ConstantMovementAction;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ActionEditorType(clazz = ConstantMovementAction.class)
public class ConstantMovementEditorControllerFactory implements ActionControllerFactory<ConstantMovementEditorController, ConstantMovementAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public ConstantMovementEditorController createFor(GameScene aGameScene, ConstantMovementAction aAction) {
        try (InputStream fxml = ConstantMovementEditorController.class.getResourceAsStream("ConstantMovementEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.constantmovement.ConstantMovementEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            ConstantMovementEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
