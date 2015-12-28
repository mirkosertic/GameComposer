package de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionEditorType;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.sound.PlaySoundAction;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.inject.Inject;

@ActionEditorType(clazz = PlaySoundAction.class)
public class PlaySoundEditorControllerFactory implements ActionControllerFactory<PlaySoundEditorController, PlaySoundAction> {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public PlaySoundEditorController createFor(GameScene aGameScene, GameRule aRule, PlaySoundAction aAction) {
        try (InputStream fxml = PlaySoundEditorController.class.getResourceAsStream("PlaySoundEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = theLoader.load(fxml);

            PlaySoundEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
