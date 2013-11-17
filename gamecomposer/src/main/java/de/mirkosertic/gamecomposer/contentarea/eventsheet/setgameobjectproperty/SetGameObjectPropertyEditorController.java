package de.mirkosertic.gamecomposer.contentarea.eventsheet.setgameobjectproperty;

import de.mirkosertic.gameengine.action.SetGameObjectPropertyAction;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.Node;

public class SetGameObjectPropertyEditorController {

    private Node view;
    private GameScene gameScene;
    private SetGameObjectPropertyAction action;

    SetGameObjectPropertyEditorController initialize(Node aView, GameScene aGameScene, SetGameObjectPropertyAction aAction) {
        view = aView;
        gameScene = aGameScene;
        action = aAction;
        return this;
    }

    public Node getView() {
        return view;
    }
}
