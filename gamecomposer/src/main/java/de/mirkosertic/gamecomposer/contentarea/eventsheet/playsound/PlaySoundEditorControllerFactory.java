package de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gameengine.action.PlaySoundAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;

public class PlaySoundEditorControllerFactory {

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    public PlaySoundEditorController createFor(GameScene aGameScene, PlaySoundAction aAction) {
        try (InputStream fxml = PlaySoundEditorController.class.getResourceAsStream("PlaySoundEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound.PlaySoundEditor");
            theLoader.setResources(theBundle);
            BorderPane theRoot = (BorderPane) theLoader.load(fxml);

            PlaySoundEditorController theController = theLoader.getController();
            return theController.initialize(theRoot, aGameScene, aAction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
