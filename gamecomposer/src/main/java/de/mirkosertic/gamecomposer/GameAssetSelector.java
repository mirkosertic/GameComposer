package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.type.ResourceName;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;
import javax.inject.Inject;

public class GameAssetSelector {

    @Inject
    PersistenceManager persistenceManager;

    public ResourceName selectAudioAssetFrom(GameScene aGameScene, Window aParentWindow) {
        FileChooser theFileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio files", "*.wav", "*.au");
        theFileChooser.getExtensionFilters().add(extFilter);
        theFileChooser.setInitialDirectory(persistenceManager.getAssetsDirectoryFor(aGameScene));

        File theSelectedFile = theFileChooser.showOpenDialog(aParentWindow);
        if (theSelectedFile == null) {
            return null;
        }

        return persistenceManager.toResourceName(aGameScene, theSelectedFile);
    }

    public ResourceName selectImageAssetFrom(GameScene aGameScene, Window aParentWindow) {
        FileChooser theFileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.png");
        theFileChooser.getExtensionFilters().add(extFilter);
        theFileChooser.setInitialDirectory(persistenceManager.getAssetsDirectoryFor(aGameScene));

        File theSelectedFile = theFileChooser.showOpenDialog(aParentWindow);
        if (theSelectedFile == null) {
            return null;
        }

        return persistenceManager.toResourceName(aGameScene, theSelectedFile);
    }
}
