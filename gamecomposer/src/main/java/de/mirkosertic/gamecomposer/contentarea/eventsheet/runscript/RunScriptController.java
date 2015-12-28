package de.mirkosertic.gamecomposer.contentarea.eventsheet.runscript;

import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.GameRule;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.script.RunScriptAction;
import javafx.fxml.FXML;
import javafx.scene.Node;

import javax.inject.Inject;

public class RunScriptController implements ActionController {

    @Inject
    EditScriptDialogFactory editScriptDialogFactory;

    private Node view;
    private GameScene gameScene;
    private GameRule gameRule;
    private RunScriptAction action;

    public RunScriptController initialize(Node aRoot, GameScene aGameScene, GameRule aRule, RunScriptAction anAction) {
        view = aRoot;
        gameScene = aGameScene;
        gameRule = aRule;
        action = anAction;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    @FXML
    public void onEditLUAScript() {
        EditScriptDialog theDialog = editScriptDialogFactory.createFor(gameScene, view, action);
        theDialog.performEditing();
    }
}