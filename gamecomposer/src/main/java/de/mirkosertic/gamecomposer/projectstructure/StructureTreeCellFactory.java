package de.mirkosertic.gamecomposer.projectstructure;

import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;
import javafx.util.Callback;

public class StructureTreeCellFactory implements Callback<TreeView, TreeCell> {

    private ContextMenuListener contextMenuListener;

    public StructureTreeCellFactory(ContextMenuListener aListener) {
        contextMenuListener = aListener;
    }

    @Override
    public TreeCell call(TreeView aTreeView) {
        return new StructureTreeCell(contextMenuListener, aTreeView);
    }
}
