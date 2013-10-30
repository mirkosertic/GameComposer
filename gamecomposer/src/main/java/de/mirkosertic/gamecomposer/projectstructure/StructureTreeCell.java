package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeView;

public class StructureTreeCell extends TreeCell {

    private TreeView treeView;

    public StructureTreeCell(TreeView aTreeView) {
        treeView = aTreeView;
    }

    @Override
    protected void updateItem(Object aValue, boolean aEmpty) {
        super.updateItem(aValue, aEmpty);
        if (aValue instanceof String) {
            setText((String) aValue);
        }
        if (aValue instanceof Game) {
            setText(((Game) aValue).getName());
        }
        if (aValue instanceof GameScene) {
            setText(((GameScene) aValue).getName());
        }
        if (aValue instanceof GameObject) {
            setText(((GameObject) aValue).getName());
        }
        if (aValue instanceof GameObjectInstance) {
            setText(((GameObjectInstance) aValue).getName());
        }
    }
}
