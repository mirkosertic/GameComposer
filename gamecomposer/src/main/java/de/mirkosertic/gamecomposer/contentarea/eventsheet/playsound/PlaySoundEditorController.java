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
package de.mirkosertic.gamecomposer.contentarea.eventsheet.playsound;

import de.mirkosertic.gamecomposer.GameAssetSelector;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.sound.PlaySoundAction;
import de.mirkosertic.gameengine.type.ResourceName;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;

import javax.inject.Inject;

public class PlaySoundEditorController implements ActionController {

    @Inject
    GameAssetSelector gameAssetSelector;

    @FXML
    TextField resourceName;

    private Node view;
    private PlaySoundAction action;
    private GameScene gameScene;

    PlaySoundEditorController initialize(Node aView, GameScene aGameScene, PlaySoundAction aAction) {
        view = aView;
        action = aAction;
        gameScene = aGameScene;

        ResourceName theResourceName = action.resourceNameProperty().get();
        if (theResourceName != null) {
            resourceName.setText(theResourceName.name);
        }

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onSelectResource() {
        ResourceName theNewResourceName = gameAssetSelector.selectAudioAssetFrom(gameScene, view.getScene().getWindow());
        if (theNewResourceName != null) {
            resourceName.setText(theNewResourceName.name);
            action.resourceNameProperty().set(theNewResourceName);
        }
    }
}
