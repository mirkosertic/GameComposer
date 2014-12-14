package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.GameObjectClipboardContent;
import de.mirkosertic.gameengine.core.*;
import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

class StructureTreeCell extends TreeCell {

    private final TreeView treeView;
    private final ContextMenuListener contextMenuListener;

    public StructureTreeCell(ContextMenuListener aContextMenuListener, TreeView aTreeView) {
        treeView = aTreeView;
        contextMenuListener = aContextMenuListener;
        setOnDragDetected(aEvent -> onDragDetected(aEvent));
    }

    private void onDragDetected(MouseEvent aEvent) {
        TreeItem theItem = getTreeItem();
        Object theValue = theItem.getValue();
        if (theValue instanceof GameObject) {

            GameObject theGameObject = (GameObject) theValue;

            Dragboard theDragboard = startDragAndDrop(TransferMode.LINK);

            ClipboardContent theContent = new ClipboardContent();
            theContent.put(GameObjectClipboardContent.FORMAT, new GameObjectClipboardContent(theGameObject.uuidProperty().get()));
            theDragboard.setContent(theContent);

            aEvent.consume();
        }
    }

    @Override
    protected void updateItem(Object aValue, boolean aEmpty) {
        super.updateItem(aValue, aEmpty);

        if (aEmpty) {
            setText(null);
            return;
        }

        if (aValue instanceof TreeObjectTypes) {
            switch((TreeObjectTypes) aValue) {
                case INSTANCES: {
                    setText("Instances");
                    setContextMenu(null);
                    break;
                }
                case OBJECTS: {
                    setText("Objects");

                    ContextMenu theContextMenu = new ContextMenu();
                    MenuItem theCreateObject = new MenuItem("New GameObject");
                    theCreateObject.setOnAction(actionEvent -> {
                        GameScene theScene = (GameScene) getTreeItem().getParent().getValue();
                        contextMenuListener.onCreateNewGameObject(theScene);
                    });
                    theContextMenu.getItems().add(theCreateObject);

                    setContextMenu(theContextMenu);
                    break;
                }
                case EVENTSHEETS: {
                    setText("Eventsheets");

                    ContextMenu theContextMenu = new ContextMenu();
                    MenuItem theCreateObject = new MenuItem("New EventSheet");
                    theCreateObject.setOnAction(actionEvent -> {
                        GameScene theScene = (GameScene) getTreeItem().getParent().getValue();
                        contextMenuListener.onNewEventSheet(theScene);
                    });
                    theContextMenu.getItems().add(theCreateObject);

                    setContextMenu(theContextMenu);
                    break;
                }
            }
        }
        if (aValue instanceof Game) {
            setText(((Game) aValue).nameProperty().get());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Create new GameScene");
            theDeleteItem.setOnAction(actionEvent -> contextMenuListener.onCreateNewGameScene());
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof GameScene) {
            final GameScene theGameScene = (GameScene) aValue;
            setText(theGameScene.nameProperty().get());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(actionEvent -> contextMenuListener.onDeleteGameScene(theGameScene));
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof GameObject) {
            final GameObject theGameObject = (GameObject) aValue;
            setText(theGameObject.nameProperty().get());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(actionEvent -> contextMenuListener.onDeleteGameObject(theGameObject));
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof EventSheet) {
            final EventSheet theEventSheet = (EventSheet) aValue;
            setText(theEventSheet.nameProperty().get());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(actionEvent -> contextMenuListener.onDeleteEventSheet(theEventSheet));
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
        if (aValue instanceof GameObjectInstance) {
            final GameObjectInstance theGameObjectInstance = (GameObjectInstance) aValue;
            setText(theGameObjectInstance.nameProperty().get());
            ContextMenu theContextMenu = new ContextMenu();
            MenuItem theDeleteItem = new MenuItem("Delete");
            theDeleteItem.setOnAction(actionEvent -> contextMenuListener.onDeleteGameObjectInstance(theGameObjectInstance));
            theContextMenu.getItems().add(theDeleteItem);
            setContextMenu(theContextMenu);
        }
    }
}