package de.mirkosertic.gamecomposer;

import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.types.ResourceName;
import javafx.stage.FileChooser;

import javax.inject.Inject;
import java.io.File;

public class GameAssetSelector {

    @Inject
    PersistenceManager persistenceManager;

    public ResourceName selectAudioAssetFrom(GameScene aGameScene) {
        FileChooser theFileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Audio files", "*.wav", "*.au");
        theFileChooser.getExtensionFilters().add(extFilter);
        theFileChooser.setInitialDirectory(persistenceManager.getAssetsDirectoryFor(aGameScene));

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile == null) {
            return null;
        }

        return persistenceManager.toResourceName(aGameScene, theSelectedFile);
    }

    public ResourceName selectImageAssetFrom(GameScene aGameScene) {
        FileChooser theFileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files", "*.png");
        theFileChooser.getExtensionFilters().add(extFilter);
        theFileChooser.setInitialDirectory(persistenceManager.getAssetsDirectoryFor(aGameScene));

        File theSelectedFile = theFileChooser.showOpenDialog(null);
        if (theSelectedFile == null) {
            return null;
        }

        return persistenceManager.toResourceName(aGameScene, theSelectedFile);
    }
}
