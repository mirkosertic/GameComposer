package de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.action.DeleteGameObjectInstanceAction;
import de.mirkosertic.gameengine.core.GameScene;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ActionEditorType(clazz = DeleteGameObjectInstanceAction.class)
public class DeleteInstanceEditorControllerFactory implements ActionControllerFactory<DeleteInstanceEditorController, DeleteGameObjectInstanceAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public DeleteInstanceEditorController createFor(GameScene aGameScene, DeleteGameObjectInstanceAction aAction) {
        try (InputStream fxml = DeleteInstanceEditorController.class.getResourceAsStream("DeleteInstanceEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance.DeleteInstanceEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            DeleteInstanceEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
