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
import java.io.IOException;

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

    ProjectStructureController initialize(Node aView) {
        projectStructureTreeView.setCellFactory(new StructureTreeCellFactory());
        projectStructureTreeView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object aOldValue, Object aNewValue) {
                Object theNewValue = null;
                if (aNewValue instanceof TreeItem) {
                    theNewValue = ((TreeItem) aNewValue).getValue();
                }
                objectSelectedEvent.fire(new ObjectSelectedEvent(theNewValue));
            }
        });
        view = aView;
        projectStructureTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseEvent(aEvent);
            }
        });
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    private void onMouseEvent(MouseEvent aEvent) {
        if (aEvent.getClickCount() == 2) {
            TreeItem theSelectedItem = (TreeItem) projectStructureTreeView.getSelectionModel().getSelectedItem();
            if (theSelectedItem.getValue() instanceof GameScene) {
                sceneSelectedEventEvent.fire(new GameSceneSelectedEvent((GameScene) theSelectedItem.getValue()));
            }
        }
    }

    public void onApplicationStarted(@Observes ApplicationStartedEvent aEvent) {
        projectStructureTreeView.setRoot(null);
    }

    public void onGameLoades(@Observes GameLoadedEvent aEvent) throws IOException {
        Game theCurrentGame = persistenceManager.getGame();

        TreeItem theRootTreeItem = new TreeItem(theCurrentGame.getName());
        theRootTreeItem.setValue(theCurrentGame);
        theRootTreeItem.setExpanded(true);

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
            }

            theSceneTreeItem.getChildren().add(theObjectsTreeItem);

            TreeItem theInstancesTreeItem = new TreeItem();
            theInstancesTreeItem.setValue("Instances");
            theInstancesTreeItem.setExpanded(true);

            for (GameObjectInstance theGameObjectInstance : theLoadedScene.getInstances()) {
                TreeItem theObjectInstanceTreeItem = new TreeItem();
                theObjectInstanceTreeItem.setValue(theGameObjectInstance);
                theInstancesTreeItem.getChildren().add(theObjectInstanceTreeItem);
            }

            theSceneTreeItem.getChildren().add(theInstancesTreeItem);

            theRootTreeItem.getChildren().add(theSceneTreeItem);
        }

        projectStructureTreeView.setRoot(theRootTreeItem);
    }
}