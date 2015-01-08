package de.mirkosertic.gamecomposer.contentarea.eventsheet.killprocessesforinstance;

import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.process.KillProcessesForInstanceAction;
import javafx.scene.Node;

public class KillProcessesForInstanceEditorController implements ActionController {

    private Node view;
    private KillProcessesForInstanceAction action;
    private GameScene gameScene;

    KillProcessesForInstanceEditorController initialize(Node aView, GameScene aGameScene, KillProcessesForInstanceAction aAction) {
        view = aView;
        action = aAction;
        gameScene = aGameScene;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}