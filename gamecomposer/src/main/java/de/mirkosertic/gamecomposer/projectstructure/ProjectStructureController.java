package de.mirkosertic.gamecomposer.projectstructure;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
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

    @FXML
    TreeView projectStructureTreeView;

    @Inject
    PersistenceManager persistenceManager;

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEvent;

    @Inject
    Event<GameSceneSelectedEvent> sceneSelectedEventEvent;

    private Node view;
    private Map<Object, TreeItem> treeItemMap;

    ProjectStructureController() {
        treeItemMap = new HashMap<>();
    }

    ProjectStructureController initialize(Node aView) {
        projectStructureTreeView.setCellFactory(new StructureTreeCellFactory());
        projectStructureTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                Object theNewValue = null;
                if (aNewValue instanceof TreeItem) {
                    theNewValue = ((TreeItem) aNewValue).getValue();
                }
                if (!(theNewValue instanceof String)) {
                    objectSelectedEvent.fire(new ObjectSelectedEvent(theNewValue));
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
                sceneSelectedEventEvent.fire(new GameSceneSelectedEvent((GameScene) theSelectedItem.getValue()));
            }
        }
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        treeItemMap.clear();
        Game theCurrentGame = persistenceManager.getGame();

        TreeItem theRootTreeItem = new TreeItem(theCurrentGame.getName());
        theRootTreeItem.setValue(theCurrentGame);
        theRootTreeItem.setExpanded(true);
        treeItemMap.put(theCurrentGame, theRootTreeItem);

        projectStructureTreeView.setRoot(theRootTreeItem);
        projectStructureTreeView.getSelectionModel().select(theRootTreeItem);

        objectSelectedEvent.fire(new ObjectSelectedEvent(theCurrentGame));
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

    public void onGameLoaded(@Observes GameLoadedEvent aEvent) {
        treeItemMap.clear();
        Game theCurrentGame = persistenceManager.getGame();

        TreeItem theRootTreeItem = new TreeItem(theCurrentGame.getName());
        theRootTreeItem.setValue(theCurrentGame);
        theRootTreeItem.setExpanded(true);
        treeItemMap.put(theCurrentGame, theRootTreeItem);

        for (String theSceneName : theCurrentGame.getScenes()) {
            TreeItem theSceneTreeItem = new TreeItem(theSceneName);
            theSceneTreeItem.setExpanded(true);

            GameScene theLoadedScene = persistenceManager.getScene(theSceneName);
            theSceneTreeItem.setValue(theLoadedScene);

            TreeItem theObjectsTreeItem = new TreeItem();
            theObjectsTreeItem.setValue("Objects");
            theObjectsTreeItem.setExpanded(true);

            for (GameObject theGameObject : theLoadedScene.getObjects()) {
                TreeItem theObjectTreeItem = new TreeItem();
                theObjectTreeItem.setValue(theGameObject);
                theObjectsTreeItem.getChildren().add(theObjectTreeItem);
                treeItemMap.put(theGameObject, theObjectsTreeItem);
            }

            theSceneTreeItem.getChildren().add(theObjectsTreeItem);

            TreeItem theInstancesTreeItem = new TreeItem();
            theInstancesTreeItem.setValue("Instances");
            theInstancesTreeItem.setExpanded(true);

            for (GameObjectInstance theGameObjectInstance : theLoadedScene.getInstances()) {
                TreeItem theObjectInstanceTreeItem = new TreeItem();
                theObjectInstanceTreeItem.setValue(theGameObjectInstance);
                theInstancesTreeItem.getChildren().add(theObjectInstanceTreeItem);
                treeItemMap.put(theGameObjectInstance, theObjectInstanceTreeItem);
            }

            theSceneTreeItem.getChildren().add(theInstancesTreeItem);

            theRootTreeItem.getChildren().add(theSceneTreeItem);
        }

        projectStructureTreeView.setRoot(theRootTreeItem);
    }
}