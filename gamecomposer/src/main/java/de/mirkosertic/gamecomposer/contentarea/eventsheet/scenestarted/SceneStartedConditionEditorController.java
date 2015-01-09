package de.mirkosertic.gamecomposer.contentarea.eventsheet.scenestarted;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gameengine.core.SceneStartedCondition;
import javafx.scene.Node;

public class SceneStartedConditionEditorController implements Controller {

    private SceneStartedCondition condition;
    private Node view;

    SceneStartedConditionEditorController initialize(Node aView, SceneStartedCondition aCondition) {
        view = aView;
        condition = aCondition;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}