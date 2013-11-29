package de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance;

import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.action.DeleteGameObjectInstanceAction;

import javafx.scene.Node;

import javax.inject.Inject;

public class DeleteInstanceEditorController implements Controller {

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private DeleteGameObjectInstanceAction action;

    DeleteInstanceEditorController initialize(Node aView, DeleteGameObjectInstanceAction aAction) {
        view = aView;
        action = aAction;

        return this;
    }

    public Node getView() {
        return view;
    }
}