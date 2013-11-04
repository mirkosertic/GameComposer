package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.GameObjectClipboardContent;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class StructureTreeCell extends TreeCell {

    private TreeView treeView;

    public StructureTreeCell(TreeView aTreeView) {
        treeView = aTreeView;
        setOnDragDetected(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onDragDetected(aEvent);
            }
        });
    }

    private void onDragDetected(MouseEvent aEvent) {
        TreeItem theItem = getTreeItem();
        Object theValue = theItem.getValue();
        if (theValue instanceof GameObject) {

            GameObject theGameObject = (GameObject) theValue;

            Dragboard theDragboard = startDragAndDrop(TransferMode.LINK);

            ClipboardContent theContent = new ClipboardContent();
            theContent.put(GameObjectClipboardContent.FORMAT, new GameObjectClipboardContent(theGameObject.getUuid()));
            theDragboard.setContent(theContent);

            aEvent.consume();
        }
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
