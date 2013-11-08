package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.GameObjectClipboardContent;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class StructureTreeCell extends TreeCell {

    private TreeView treeView;
    private ContextMenuListener contextMenuListener;

    public StructureTreeCell(ContextMenuListener aContextMenuListener, TreeView aTreeView) {
        treeView = aTreeView;
        contextMenuListener = aContextMenuListener;
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
        if (aValue instanceof TreeObjectTypes) {
            switch((TreeObjectTypes) aValue) {
                case INSTANCES:
                    setText("Instances");
                    setContextMenu(null);
                    break;
                case OBJECTS:
                    setText("Objects");

                    ContextMenu theContextMenu = new ContextMenu();
                    MenuItem theCreateObject = new MenuItem("New GameObject");
                    theCreateObject.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            GameScene theScene = (GameScene) getTreeItem().getParent().getValue();
                            contextMenuListener.onCreateNewGameObject(theScene);
                        }
                    });
                    theContextMenu.getItems().add(theCreateObject);

                    setContextMenu(theContextMenu);
                    break;
            }
        }
        if (aValue instanceof Game) {
            setText(((Game) aValue).getName());
            setContextMenu(null);
        }
        if (aValue instanceof GameScene) {
            final GameScene theGameScene = (GameScene) aValue;
            setText(theGameScene.getName());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenuListener.onDeleteGameScene(theGameScene);
                }
            });
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof GameObject) {
            final GameObject theGameObject = (GameObject) aValue;
            setText(theGameObject.getName());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenuListener.onDeleteGameObject(theGameObject);
                }
            });
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof GameObjectInstance) {
            final GameObjectInstance theGameObjectInstance = (GameObjectInstance) aValue;
            setText(theGameObjectInstance.getName());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    contextMenuListener.onDeleteGameObjectInstance(theGameObjectInstance);
                }
            });
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
    }
}