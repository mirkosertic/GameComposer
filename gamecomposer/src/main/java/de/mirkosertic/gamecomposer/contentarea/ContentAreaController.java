package de.mirkosertic.gamecomposer.contentarea;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.EventSheetEditorController;
import de.mirkosertic.gamecomposer.contentarea.eventsheet.EventSheetEditorControllerFactory;
import de.mirkosertic.gamecomposer.contentarea.gamescene.GameSceneEditorController;
import de.mirkosertic.gamecomposer.contentarea.gamescene.GameSceneEditorControllerFactory;
import de.mirkosertic.gameengine.core.EventSheet;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.PropertyChangeEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class ContentAreaController implements ChildController {

    @FXML
    TabPane editorTabPane;

    @FXML
    BorderPane welcomeBorderPane;

    @Inject
    GameSceneEditorControllerFactory sceneEditorControllerFactory;

    @Inject
    EventSheetEditorControllerFactory eventSheetEditorControllerFactory;

    private Node view;
    private Map<ContentChildController, Tab> activeTabs;

    public ContentAreaController() {
        activeTabs = new HashMap<>();
    }

    ContentAreaController initialize(Node aView) {
        view = aView;
        editorTabPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent aKeyEvent) {
                Tab theSelectedTab = editorTabPane.getSelectionModel().getSelectedItem();
                if (theSelectedTab != null) {
                    for (Map.Entry<ContentChildController, Tab> theTab : activeTabs.entrySet()) {
                        if (theTab.getValue() == theSelectedTab) {
                            theTab.getKey().processKeyPressedEvent(aKeyEvent);
                        }
                    }
                }
            }
        });
        editorTabPane.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent aKeyEvent) {
                Tab theSelectedTab = editorTabPane.getSelectionModel().getSelectedItem();
                if (theSelectedTab != null) {
                    for (Map.Entry<ContentChildController, Tab> theTab : activeTabs.entrySet()) {
                        if (theTab.getValue() == theSelectedTab) {
                            theTab.getKey().processKeyReleasedEvent(aKeyEvent);
                        }
                    }
                }
            }
        });
        return this;
    }

    @Override
    public Node getView() {
        return view;
    }

    public void onApplicationStarted(@Observes ApplicationStartedEvent aEvent) {
        editorTabPane.getTabs().clear();
        editorTabPane.setVisible(false);
        welcomeBorderPane.setVisible(true);
    }

    public void onNewGameEvent(@Observes NewGameEvent aEvent) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().removed();
            editorTabPane.getTabs().remove(theTabEntry.getValue());
        }
        editorTabPane.setVisible(false);
        welcomeBorderPane.setVisible(true);
    }

    void addTab(final Tab aTab) {
        editorTabPane.setVisible(true);
        welcomeBorderPane.setVisible(false);
        editorTabPane.getTabs().add(aTab);
        aTab.setClosable(true);
        aTab.setOnClosed(new EventHandler<Event>() {
            @Override
            public void handle(Event aEvent) {
                onTabClose(aTab);
            }
        });
    }

    void onTabClose(Tab aTab) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getValue() == aTab) {
                theTabEntry.getKey().removed();
                activeTabs.remove(theTabEntry.getKey());
                return;
            }
        }
    }

    public void onObjectUpdated(@Observes PropertyChangeEvent aEvent) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onObjectUpdated(theTabEntry.getValue(), aEvent);
        }
    }

    public void onShutdown(@Observes ShutdownEvent aEvent) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onShutdown(aEvent);
        }
    }

    public void onObjectSelected(@Observes ObjectSelectedEvent aEvent) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onObjectSelected(aEvent);
        }
    }

    public void onEventSheetSelected(@Observes EventSheetSelectedEvent aEvent) {
        EventSheet theSheet = aEvent.getEventSheet();
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getKey().getEditingObject() == theSheet) {
                editorTabPane.getSelectionModel().select(theTabEntry.getValue());
                return;
            }
        }

        EventSheetEditorController theController = eventSheetEditorControllerFactory.createFor(theSheet);
        Tab theTab = new Tab(theSheet.nameProperty().get());
        theTab.setContent(theController.getView());

        addTab(theTab);

        activeTabs.put(theController, theTab);

        theController.addedAsTab();
    }

    public void onGameSceneSelected(@Observes GameSceneSelectedEvent aEvent) {
        GameScene theScene = aEvent.getScene();
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            if (theTabEntry.getKey().getEditingObject() == theScene) {
                editorTabPane.getSelectionModel().select(theTabEntry.getValue());
                return;
            }
        }

        GameSceneEditorController theSceneEditorController = sceneEditorControllerFactory.createFor(theScene);
        Tab theTab = new Tab(theScene.nameProperty().get());
        theTab.setContent(theSceneEditorController.getView());

        addTab(theTab);

        activeTabs.put(theSceneEditorController, theTab);

        theSceneEditorController.addedAsTab();
    }

    public void onFlushResourceCache(@Observes FlushResourceCacheEvent aEvent) {
        for (Map.Entry<ContentChildController, Tab> theTabEntry : activeTabs.entrySet()) {
            theTabEntry.getKey().onFlushResourceCache(aEvent);
        }
    }
}