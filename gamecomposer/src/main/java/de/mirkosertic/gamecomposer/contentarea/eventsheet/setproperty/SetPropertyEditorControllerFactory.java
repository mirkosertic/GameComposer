package de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.SetPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

@ActionEditorType(clazz = SetPropertyAction.class)
public class SetPropertyEditorControllerFactory implements ActionControllerFactory<SetPropertyEditorController, SetPropertyAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public SetPropertyEditorController createFor(GameScene aGameScene, GameRule aRule, SetPropertyAction aAction) {
        try (InputStream fxml = SetPropertyEditorController.class.getResourceAsStream("SetPropertyEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.setproperty.SetPropertyEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            SetPropertyEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aRule, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
