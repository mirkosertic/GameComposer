package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.ChildController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeView;

public class ProjectStructureController implements ChildController {

    @FXML
    TreeView projectStructureTreeView;

    private Node view;

    ProjectStructureController initialize(Node aView) {
        view = aView;
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }
}
