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

import de.mirkosertic.gamecomposer.ApplicationStartedEvent;
import de.mirkosertic.gamecomposer.Controller;
import de.mirkosertic.gamecomposer.EventSheetSelectedEvent;
import de.mirkosertic.gamecomposer.GameLoadedEvent;
import de.mirkosertic.gamecomposer.GameSceneCreatedEvent;
import de.mirkosertic.gamecomposer.GameSceneDeletedEvent;
import de.mirkosertic.gamecomposer.GameSceneSelectedEvent;
import de.mirkosertic.gamecomposer.MessageBox;
import de.mirkosertic.gamecomposer.NewGameEvent;
import de.mirkosertic.gamecomposer.NewGameSceneEvent;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.PersistenceManager;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.EventSheetAddedToScene;
import de.mirkosertic.gameengine.core.EventSheetRemovedFromScene;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameObjectInstanceAddedToScene;
import de.mirkosertic.gameengine.core.GameObjectInstanceRemovedFromScene;
import de.mirkosertic.gameengine.core.GameObjectRemovedFromScene;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.PropertyChanged;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ProjectStructureController implements Controller {

    @FXML
    TreeView projectStructureTreeView;

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<Object> eventGateway;

    @Inject
    MessageBox messageBox;

    private Node view;
    private final Map<Object, TreeItem> treeItemMap;

    ProjectStructureController() {
        treeItemMap = new HashMap<>();
    }

    ProjectStructureController initialize(Node aView) {
        projectStructureTreeView.setCellFactory(new StructureTreeCellFactory(new ContextMenuListener() {
            @Override
            public void onDeleteGameScene(GameScene aGameScene) {
                if (messageBox.showMessageBox(view, "Delete scene", "Do you really want to delete the scene?", MessageBox.ButtonType.YES, MessageBox.ButtonType.NO) == MessageBox.ButtonType.YES) {
                    persistenceManager.deleteScene(aGameScene);
                    initializeTree(persistenceManager.getGame());
                }
            }

            @Override
            public void onDeleteGameObject(GameObject aGameObject) {
                aGameObject.getGameScene().removeGameObject(aGameObject);
            }

            @Override
            public void onDeleteGameObjectInstance(GameObjectInstance aGameObjectInstance) {
                aGameObjectInstance.getOwnerGameObject().getGameScene().removeGameObjectInstance(aGameObjectInstance);
            }

            @Override
            public void onCreateNewGameObject(GameScene aGameScene) {
                // Selecting this object will be done by the the game event handler registered below.
                aGameScene.createNewGameObject("New Object");
            }

            @Override
            public void onCreateNewGameScene() {
                eventGateway.fire(new NewGameSceneEvent());
            }

            @Override
            public void onNewEventSheet(GameScene aGameScene) {
                EventSheet theSheet = aGameScene.createNewEventSheet();

                initializeTree(persistenceManager.getGame());
                projectStructureTreeView.getSelectionModel().select(treeItemMap.get(theSheet));
            }

            @Override
            public void onDeleteEventSheet(EventSheet aEventSheet) {
                aEventSheet.getGameScene().removeEventSheet(aEventSheet);
            }
        }));
        projectStructureTreeView.getSelectionModel().selectedItemProperty().addListener((observableValue, aOldValue, aNewValue) -> {
            Object theNewValue = null;
            if (aNewValue instanceof TreeItem) {
                theNewValue = ((TreeItem) aNewValue).getValue();
            }
            if (!(theNewValue instanceof TreeObjectTypes)) {
                eventGateway.fire(new ObjectSelectedEvent(theNewValue));
            }
        });
        view = aView;
        projectStructureTreeView.setOnMouseClicked(aEvent -> onMouseClicked(aEvent));
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    private void onMouseClicked(MouseEvent aEvent) {
        TreeItem theSelectedItem = (TreeItem) projectStructureTreeView.getSelectionModel().getSelectedItem();
        if (aEvent.getClickCount() == 2) {
            if (theSelectedItem.getValue() instanceof GameScene) {
                eventGateway.fire(new GameSceneSelectedEvent((GameScene) theSelectedItem.getValue()));
            }
            if (theSelectedItem.getValue() instanceof EventSheet) {
                eventGateway.fire(new EventSheetSelectedEvent((EventSheet) theSelectedItem.getValue()));
            }
        }
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        treeItemMap.clear();
        Game theCurrentGame = aEvent.getGame();

        TreeItem theRootTreeItem = new TreeItem(theCurrentGame.nameProperty().get());
        theRootTreeItem.setValue(theCurrentGame);
        theRootTreeItem.setExpanded(true);
        treeItemMap.put(theCurrentGame, theRootTreeItem);

        projectStructureTreeView.setRoot(theRootTreeItem);
        projectStructureTreeView.getSelectionModel().select(theRootTreeItem);

        eventGateway.fire(new ObjectSelectedEvent(theCurrentGame));
    }

    public void onApplicationStarted(@Observes ApplicationStartedEvent aEvent) {
        projectStructureTreeView.setRoot(null);
    }

    public void onObjectSelected(@Observes ObjectSelectedEvent aEvent) {
        TreeItem theItem = treeItemMap.get(aEvent.getSelectedObject());
        TreeItem theCurrentSelectedItem = (TreeItem) projectStructureTreeView.getSelectionModel().getSelectedItem();
        if (theItem != null && theItem != theCurrentSelectedItem) {
            projectStructureTreeView.getSelectionModel().select(theItem);
            projectStructureTreeView.scrollTo(projectStructureTreeView.getRow(theItem));
        }
    }

    public void onGameSceneDeleted(@Observes GameSceneDeletedEvent aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onGameObjectAdded(@Observes GameObjectAddedToScene aEvent) {
        initializeTree(persistenceManager.getGame());
        projectStructureTreeView.getSelectionModel().select(treeItemMap.get(aEvent.object));
    }

    public void onGameObjectRemoved(@Observes GameObjectRemovedFromScene aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onEventSheetAdded(@Observes EventSheetAddedToScene aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onGameObjectInstanceAdded(@Observes GameObjectInstanceAddedToScene aEvent) {
        initializeTree(persistenceManager.getGame());
        projectStructureTreeView.getSelectionModel().select(treeItemMap.get(aEvent.instance));
    }

    public void onGameObjectInstanceRemoved(@Observes GameObjectInstanceRemovedFromScene aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onEventSheetRemoved(@Observes EventSheetRemovedFromScene aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onObjectUpdatedEvent(@Observes PropertyChanged aEvent) {
        TreeItem theItem = treeItemMap.get(aEvent.getOwner());
        if (theItem != null) {
            theItem.setValue(null);
            theItem.setValue(aEvent.getOwner());
        }
    }

    public void onNewGameSceneCreated(@Observes GameSceneCreatedEvent aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    public void onGameLoaded(@Observes GameLoadedEvent aEvent) {
        initializeTree(persistenceManager.getGame());
    }

    private void initializeTree(Game aGame) {
        treeItemMap.clear();

        TreeItem theRootTreeItem = new TreeItem(aGame.nameProperty().get());
        theRootTreeItem.setValue(aGame);
        theRootTreeItem.setExpanded(true);
        treeItemMap.put(aGame, theRootTreeItem);

        for (String theSceneIDs : persistenceManager.getScenes()) {

            GameScene theLoadedScene = persistenceManager.getScene(theSceneIDs);

            TreeItem theSceneTreeItem = new TreeItem(theSceneIDs);
            theSceneTreeItem.setExpanded(true);
            theSceneTreeItem.setValue(theLoadedScene);

            TreeItem theObjectsTreeItem = new TreeItem();
            theObjectsTreeItem.setValue(TreeObjectTypes.OBJECTS);
            theObjectsTreeItem.setExpanded(true);

            List<GameObject> theSortedGameObjects = Arrays.asList(theLoadedScene.getObjects());
            Collections.sort(theSortedGameObjects, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));

            for (GameObject theGameObject : theSortedGameObjects) {
                TreeItem theObjectTreeItem = new TreeItem();
                theObjectTreeItem.setValue(theGameObject);
                theObjectsTreeItem.getChildren().add(theObjectTreeItem);
                treeItemMap.put(theGameObject, theObjectTreeItem);
            }

            theSceneTreeItem.getChildren().add(theObjectsTreeItem);

            TreeItem theEventSheetsTreeItem = new TreeItem();
            theEventSheetsTreeItem.setValue(TreeObjectTypes.EVENTSHEETS);
            theEventSheetsTreeItem.setExpanded(true);

            for (EventSheet theEventSheet : theLoadedScene.getEventSheets()) {
                TreeItem theSheetTreeItem = new TreeItem();
                theSheetTreeItem.setValue(theEventSheet);
                theEventSheetsTreeItem.getChildren().add(theSheetTreeItem);
                treeItemMap.put(theEventSheet, theSheetTreeItem);
            }

            theSceneTreeItem.getChildren().add(theEventSheetsTreeItem);

            TreeItem theInstancesTreeItem = new TreeItem();
            theInstancesTreeItem.setValue(TreeObjectTypes.INSTANCES);
            theInstancesTreeItem.setExpanded(true);

            List<GameObjectInstance> theSortedInstances = Arrays.asList(theLoadedScene.getInstances());
            Collections.sort(theSortedInstances, (o1, o2) -> o1.nameProperty().get().compareTo(o2.nameProperty().get()));

            for (GameObjectInstance theGameObjectInstance : theSortedInstances) {
                TreeItem theObjectInstanceTreeItem = new TreeItem();
                theObjectInstanceTreeItem.setValue(theGameObjectInstance);
                theInstancesTreeItem.getChildren().add(theObjectInstanceTreeItem);
                treeItemMap.put(theGameObjectInstance, theObjectInstanceTreeItem);
            }

            theSceneTreeItem.getChildren().add(theInstancesTreeItem);
            theRootTreeItem.getChildren().add(theSceneTreeItem);

            treeItemMap.put(theLoadedScene, theSceneTreeItem);
        }

        projectStructureTreeView.setRoot(theRootTreeItem);
    }
}