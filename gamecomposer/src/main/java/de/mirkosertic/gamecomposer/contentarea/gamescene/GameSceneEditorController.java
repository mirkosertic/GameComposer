package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.*;
import de.mirkosertic.gamecomposer.contentarea.ContentController;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.physics.DisableDynamicPhysics;
import de.mirkosertic.gameengine.physics.EnableDynamicPhysics;
import de.mirkosertic.gameengine.types.Position;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.input.*;
import javafx.scene.layout.BorderPane;

import javax.enterprise.event.Event;
import java.util.List;

public class GameSceneEditorController implements ContentController<GameScene> {

    @FXML
    BorderPane centerBorderPane;

    @FXML
    CheckBox snapToGrid;

    @FXML
    CheckBox renderPhysics;

    @FXML
    TextField gridsizeWidth;

    @FXML
    TextField gridsizeHeight;

    private GameScene gameScene;
    private Node view;
    private EditorJXGameView gameView;
    private GameLoop gameLoop;
    private CameraComponent cameraComponent;

    private Event<ObjectSelectedEvent> objectSelectedEventEvent;

    private GameObjectInstance dndCreateInstance;
    private GameObjectInstance draggingInstance;
    private Position draggingMouseWorldPosition;
    private GameRuntime gameRuntime;

    GameSceneEditorController initialize(GameRuntime aGameRuntime, GameScene aScene, Node aView, EditorJXGameView aGameView, GameLoop aGameLoop, CameraComponent aCameraComponent, Event<ObjectSelectedEvent> aSelectedEvent) {

        gameRuntime = aGameRuntime;

        centerBorderPane.setCenter(aGameView);

        gameScene = aScene;
        view = aView;
        gameView = aGameView;
        gameLoop = aGameLoop;
        cameraComponent = aCameraComponent;
        objectSelectedEventEvent = aSelectedEvent;

        gameView.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent aEvent) {
                onDragOver(aEvent);
            }
        });
        gameView.setOnDragEntered(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent aEvent) {
                onDragEntered(aEvent);
            }
        });
        gameView.setOnDragExited(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent aEvent) {
                onDragExited(aEvent);
            }
        });
        gameView.setOnDragDropped(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent aEvent) {
                setOnDragDropped(aEvent);
            }
        });
        gameView.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseDragged(aEvent);
            }
        });
        gameView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMousePressed(aEvent);
            }
        });
        gameView.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseReleased(aEvent);
            }
        });
        gameView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent aEvent) {
                onMouseClicked(aEvent);
            }
        });

        gridsizeWidth.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameView.gridsizeWidthProperty().set(Integer.valueOf(aNewValue));
            }
        });
        gridsizeHeight.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String aOldValue, String aNewValue) {
                gameView.gridsizeHeightProperty().set(Integer.valueOf(aNewValue));
            }
        });
        gridsizeWidth.setText(Integer.toString(gameView.gridsizeWidthProperty().get()));
        gridsizeHeight.setText(Integer.toString(gameView.gridsizeHeightProperty().get()));

        gameView.renderPhysicsDebugProperty().bind(renderPhysics.selectedProperty());
        gameView.snapToGridProperty().bind(snapToGrid.selectedProperty());
        return this;
    }

    private void onDragOver(DragEvent aEvent) {
        if (aEvent.getGestureSource() != this && aEvent.getDragboard().hasContent(GameObjectClipboardContent.FORMAT)) {
            aEvent.acceptTransferModes(TransferMode.LINK);
        }

        if (dndCreateInstance != null) {
            Position theNewPosition = cameraComponent.transformFromScreen(new Position(aEvent.getX(), aEvent.getY()));
            dndCreateInstance.positionProperty().set(theNewPosition);
        }

        aEvent.consume();
    }

    private void onDragEntered(DragEvent aEvent) {
        Dragboard theDragBoard = aEvent.getDragboard();
        if (theDragBoard.hasContent(GameObjectClipboardContent.FORMAT)) {
            GameObjectClipboardContent theContent = (GameObjectClipboardContent) theDragBoard.getContent(GameObjectClipboardContent.FORMAT);
            GameObject theGameObject = gameScene.findGameObjectByID(theContent.getGameObjectId());

            GameObjectInstance theInstance = gameScene.createFrom(theGameObject);
            theInstance.positionProperty().set(cameraComponent.transformFromScreen(new Position(aEvent.getX(), aEvent.getY())));

            gameScene.addGameObjectInstance(theInstance);

            gameScene.getRuntime().getEventManager().fire(new DisableDynamicPhysics(theInstance));

            dndCreateInstance = theInstance;

            aEvent.consume();
        }
    }

    private void onDragExited(DragEvent aEvent) {
        if (dndCreateInstance != null) {
            gameScene.removeGameObjectInstance(dndCreateInstance);
            dndCreateInstance = null;
            aEvent.consume();
        }
    }

    private void setOnDragDropped(DragEvent aEvent) {
        if (dndCreateInstance != null) {
            aEvent.setDropCompleted(true);
            aEvent.consume();
            if (snapToGrid.isSelected()) {
                dndCreateInstance.positionProperty().set(gameView.snapToGrid(dndCreateInstance.positionProperty().get()));
            }

            gameScene.getRuntime().getEventManager().fire(new EnableDynamicPhysics(dndCreateInstance));

            dndCreateInstance = null;
        }
    }

    private void onMousePressed(MouseEvent aEvent) {
        Position theScreenPosition = new Position(aEvent.getX(), aEvent.getY());
        Position theWorldPosition = cameraComponent.transformFromScreen(theScreenPosition);
        List<GameObjectInstance> theFoundInstances = gameScene.findAllAt(theWorldPosition);
        if (theFoundInstances.size() == 1) {
            draggingInstance = theFoundInstances.get(0);
            draggingMouseWorldPosition = theWorldPosition;

            gameScene.getRuntime().getEventManager().fire(new DisableDynamicPhysics(draggingInstance));

            objectSelectedEventEvent.fire(new ObjectSelectedEvent(draggingInstance));
        } else {
            draggingInstance = null;
            draggingMouseWorldPosition = theScreenPosition;
        }
    }

    private void onMouseDragged(MouseEvent aEvent) {
        if (draggingMouseWorldPosition != null) {
            if (draggingInstance != null) {
                // Move object instance

                Position theScreenPosition = new Position(aEvent.getX(), aEvent.getY());
                Position theWorldPosition = cameraComponent.transformFromScreen(theScreenPosition);
                float theDX = theWorldPosition.x - draggingMouseWorldPosition.x;
                float theDY = theWorldPosition.y - draggingMouseWorldPosition.y;

                Position theObjectPosition = draggingInstance.positionProperty().get();
                draggingInstance.positionProperty().set(new Position(theObjectPosition.x + theDX, theObjectPosition.y + theDY));

                draggingMouseWorldPosition = theWorldPosition;
            } else {

                Position theScreenPosition = new Position(aEvent.getX(), aEvent.getY());

                float theDX = theScreenPosition.x - draggingMouseWorldPosition.x;
                float theDY = theScreenPosition.y - draggingMouseWorldPosition.y;

                // Move camera
                Position theObjectPosition = cameraComponent.getObjectInstance().positionProperty().get();
                cameraComponent.getObjectInstance().positionProperty().set(new Position(theObjectPosition.x - theDX, theObjectPosition.y - theDY));

                draggingMouseWorldPosition = theScreenPosition;
            }
        }
    }

    private void onMouseReleased(MouseEvent aEvent) {
        if (draggingInstance != null) {
            if (snapToGrid.isSelected()) {
                draggingInstance.positionProperty().set(gameView.snapToGrid(draggingInstance.positionProperty().get()));
            }
            gameScene.getRuntime().getEventManager().fire(new EnableDynamicPhysics(draggingInstance));
        }
        draggingMouseWorldPosition = null;
        draggingInstance = null;
    }

    @Override
    public GameScene getEditingObject() {
        return gameScene;
    }

    @Override
    public void processKeyPressedEvent(KeyEvent aKeyEvent) {
        gameScene.getRuntime().getEventManager().fire(new KeyPressed(GameKeyCode.valueOf(aKeyEvent.getCode().name())));
    }

    @Override
    public void processKeyReleasedEvent(KeyEvent aKeyEvent) {
        gameScene.getRuntime().getEventManager().fire(new KeyReleased(GameKeyCode.valueOf(aKeyEvent.getCode().name())));
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void addedAsTab() {
        gameView.startTimer(gameLoop);
    }

    @Override
    public void removed() {
        gameScene.getRuntime().getEventManager().fire(new GameShutdown());
        gameView.stopTimer();
    }

    @Override
    public void onObjectSelected(ObjectSelectedEvent aEvent) {
        gameView.onObjectSelected(aEvent);
    }

    @Override
    public void onShutdown(ShutdownEvent aEvent) {
        gameView.stopTimer();
        gameScene.getRuntime().getEventManager().fire(new GameShutdown());
    }

    void onMouseClicked(MouseEvent aEvent) {
        Position theClickPosition = new Position(aEvent.getX(), aEvent.getY());
        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(gameScene)) {
            if (theInstance.contains(theClickPosition)) {
                objectSelectedEventEvent.fire(new ObjectSelectedEvent(theInstance));
            }
        }
    }

    public void onObjectUpdated(Tab aTab, PropertyChanged aEvent) {
        if (aEvent.getOwner() == gameScene) {
            aTab.setText(gameScene.nameProperty().get());
        }
    }

    @Override
    public void onFlushResourceCache(FlushResourceCacheEvent aEvent) {
        gameRuntime.getResourceCache().flush();
    }
}