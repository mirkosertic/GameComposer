package de.mirkosertic.gamecomposer.projectstructure;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class StructureTreeCellFactory implements Callback<TreeView, TreeCell> {

    @Override
    public TreeCell call(TreeView aTreeView) {
        return new StructureTreeCell(aTreeView);
    }
}
