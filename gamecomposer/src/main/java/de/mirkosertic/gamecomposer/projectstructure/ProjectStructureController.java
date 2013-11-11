package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.PropertyChangeEvent;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ProjectStructureController implements ChildController {

    class SceneTreeDescriptor {
        TreeItem sceneTreeItem;
        TreeItem objectsItem;
        TreeItem instancesItem;
    }

    @FXML
    TreeView projectStructureTreeView;

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<Object> eventGateway;

    private Node view;
    private Map<GameScene, SceneTreeDescriptor> sceneTreeDescriptorMap;
    private Map<Object, TreeItem> treeItemMap;

    ProjectStructureController() {
        treeItemMap = new HashMap<>();
        sceneTreeDescriptorMap = new HashMap<>();
    }

    ProjectStructureController initialize(Node aView) {
        projectStructureTreeView.setCellFactory(new StructureTreeCellFactory(new ContextMenuListener() {
            @Override
            public void onDeleteGameScene(GameScene aGameScene) {
                // TODO:
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
                GameObject theGameObject = new GameObject(aGameScene, "New Object");
                aGameScene.addGameObject(theGameObject);
            }

            @Override
            public void onCreateNewGameScene() {
                eventGateway.fire(new NewGameSceneEvent());
            }
        }));
        projectStructureTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                Object theNewValue = null;
                if (aNewValue instanceof TreeItem) {
                    theNewValue = ((TreeItem) aNewValue).getValue();
                }
                if (!(theNewValue instanceof TreeObjectTypes)) {
                    eventGateway.fire(new ObjectSelectedEvent(theNewValue));
                }
            }
        });
        view = aView;
        projectStructureTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseClicked(aEvent);
            }
        });
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
        }
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        treeItemMap.clear();
        Game theCurrentGame = persistenceManager.getGame();

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

    public void onGameObjectAdded(@Observes GameObjectAddedToSceneEvent aEvent) {
        GameScene theScene = aEvent.getGameObject().getGameScene();
        SceneTreeDescriptor theDesc = sceneTreeDescriptorMap.get(theScene);

        TreeItem theObjectTreeItem = new TreeItem();
        theObjectTreeItem.setValue(aEvent.getGameObject());
        theDesc.objectsItem.getChildren().add(theObjectTreeItem);
        treeItemMap.put(aEvent.getGameObject(), theObjectTreeItem);
    }

    public void onGameObjectRemoved(@Observes GameObjectRemovedFromSceneEvent aEvent) {
        GameScene theScene = aEvent.getGameObject().getGameScene();
        SceneTreeDescriptor theDesc = sceneTreeDescriptorMap.get(theScene);

        TreeItem theObjectTreeItem = treeItemMap.get(aEvent.getGameObject());
        theDesc.objectsItem.getChildren().remove(theObjectTreeItem);

        treeItemMap.remove(aEvent.getGameObject());
    }

    public void onGameObjectInstanceAdded(@Observes GameObjectInstanceAddedToSceneEvent aEvent) {
        GameScene theScene = aEvent.getGameObjectInstance().getOwnerGameObject().getGameScene();
        SceneTreeDescriptor theDesc = sceneTreeDescriptorMap.get(theScene);

        TreeItem theObjectTreeItem = new TreeItem();
        theObjectTreeItem.setValue(aEvent.getGameObjectInstance());
        theDesc.instancesItem.getChildren().add(theObjectTreeItem);
        treeItemMap.put(aEvent.getGameObjectInstance(), theObjectTreeItem);
    }

    public void onGameObjectInstanceRemoved(@Observes GameObjectInstanceRemovedFromSceneEvent aEvent) {
        GameScene theScene = aEvent.getGameObjectInstance().getOwnerGameObject().getGameScene();
        SceneTreeDescriptor theDesc = sceneTreeDescriptorMap.get(theScene);

        TreeItem theObjectTreeItem = treeItemMap.get(aEvent.getGameObjectInstance());
        theDesc.objectsItem.getChildren().remove(theObjectTreeItem);

        treeItemMap.remove(aEvent.getGameObjectInstance());
    }

    public void onObjectUpdatedEvent(@Observes PropertyChangeEvent aEvent) {
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
        sceneTreeDescriptorMap.clear();

        TreeItem theRootTreeItem = new TreeItem(aGame.nameProperty().get());
        theRootTreeItem.setValue(aGame);
        theRootTreeItem.setExpanded(true);
        treeItemMap.put(aGame, theRootTreeItem);

        for (String theSceneIDs : aGame.getScenes()) {

            GameScene theLoadedScene = persistenceManager.getScene(theSceneIDs);

            TreeItem theSceneTreeItem = new TreeItem(theSceneIDs);
            theSceneTreeItem.setExpanded(true);
            theSceneTreeItem.setValue(theLoadedScene);

            TreeItem theObjectsTreeItem = new TreeItem();
            theObjectsTreeItem.setValue(TreeObjectTypes.OBJECTS);
            theObjectsTreeItem.setExpanded(true);

            for (GameObject theGameObject : theLoadedScene.getObjects()) {
                TreeItem theObjectTreeItem = new TreeItem();
                theObjectTreeItem.setValue(theGameObject);
                theObjectsTreeItem.getChildren().add(theObjectTreeItem);
                treeItemMap.put(theGameObject, theObjectTreeItem);
            }

            theSceneTreeItem.getChildren().add(theObjectsTreeItem);

            TreeItem theInstancesTreeItem = new TreeItem();
            theInstancesTreeItem.setValue(TreeObjectTypes.INSTANCES);
            theInstancesTreeItem.setExpanded(true);

            for (GameObjectInstance theGameObjectInstance : theLoadedScene.getInstances()) {
                TreeItem theObjectInstanceTreeItem = new TreeItem();
                theObjectInstanceTreeItem.setValue(theGameObjectInstance);
                theInstancesTreeItem.getChildren().add(theObjectInstanceTreeItem);
                treeItemMap.put(theGameObjectInstance, theObjectInstanceTreeItem);
            }

            theSceneTreeItem.getChildren().add(theInstancesTreeItem);
            theRootTreeItem.getChildren().add(theSceneTreeItem);

            SceneTreeDescriptor theDesc = new SceneTreeDescriptor();
            theDesc.sceneTreeItem = theSceneTreeItem;
            theDesc.objectsItem = theObjectsTreeItem;
            theDesc.instancesItem = theInstancesTreeItem;

            sceneTreeDescriptorMap.put(theLoadedScene, theDesc);
            treeItemMap.put(theLoadedScene, theSceneTreeItem);
        }

        projectStructureTreeView.setRoot(theRootTreeItem);
    }
}