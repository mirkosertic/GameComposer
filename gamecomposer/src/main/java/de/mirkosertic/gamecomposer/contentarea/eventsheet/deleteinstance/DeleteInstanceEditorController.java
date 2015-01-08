package de.mirkosertic.gamecomposer.contentarea.eventsheet.deleteinstance;

import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.ActionController;
import de.mirkosertic.gameengine.core.DeleteGameObjectInstanceAction;
import javafx.scene.Node;

import javax.inject.Inject;

public class DeleteInstanceEditorController implements ActionController {

    @Inject
    PersistenceManager persistenceManager;

    private Node view;
    private DeleteGameObjectInstanceAction action;

    DeleteInstanceEditorController initialize(Node aView, DeleteGameObjectInstanceAction aAction) {
        view = aView;
        action = aAction;

        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}