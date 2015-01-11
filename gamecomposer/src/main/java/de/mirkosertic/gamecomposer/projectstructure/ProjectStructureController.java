package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.PropertyChanged;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;

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