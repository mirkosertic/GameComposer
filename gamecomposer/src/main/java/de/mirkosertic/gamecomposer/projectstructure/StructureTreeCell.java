/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.GameObjectClipboardContent;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
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