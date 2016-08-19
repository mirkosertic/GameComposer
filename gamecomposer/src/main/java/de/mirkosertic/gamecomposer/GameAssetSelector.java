/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
