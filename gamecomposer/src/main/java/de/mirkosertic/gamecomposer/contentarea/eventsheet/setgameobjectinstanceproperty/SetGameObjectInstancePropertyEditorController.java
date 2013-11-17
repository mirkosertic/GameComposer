package de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectinstanceproperty;

import de.mirkosertic.gameengine.action.SetGameObjectInstancePropertyAction;
import de.mirkosertic.gameengine.core.GameScene;

import javafx.scene.Node;

public class SetGameObjectInstancePropertyEditorController {

    private GameScene gameScene;
    private SetGameObjectInstancePropertyAction action;
    private Node view;

    SetGameObjectInstancePropertyEditorController initialize(Node aView, GameScene aGameScene, SetGameObjectInstancePropertyAction aAction) {
        gameScene = aGameScene;
        view = aView;
        action = aAction;
        return this;
    }

    public Node getView() {
        return view;
    }
}
