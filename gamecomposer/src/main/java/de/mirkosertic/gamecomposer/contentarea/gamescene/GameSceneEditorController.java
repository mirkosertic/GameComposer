package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.ShutdownEvent;
import de.mirkosertic.gamecomposer.contentarea.ContentChildController;
import de.mirkosertic.gameengine.camera.CameraComponent;
import de.mirkosertic.gameengine.core.*;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javax.enterprise.event.Event;

public class GameSceneEditorController implements ContentChildController<GameScene> {

    private GameScene gameScene;
    private Node view;
    private EditorJXGameView gameView;
    private Thread gameLoopThread;
    private CameraComponent cameraComponent;
    private Event<ObjectSelectedEvent> objectSelectedEventEvent;

    GameSceneEditorController(GameScene aScene, Node aView, EditorJXGameView aGameView, Thread aGameLoopThread, CameraComponent aCameraComponent, Event<ObjectSelectedEvent> aSelectedEvent) {
        gameScene = aScene;
        view = aView;
        gameView = aGameView;
        gameLoopThread = aGameLoopThread;
        cameraComponent = aCameraComponent;
        objectSelectedEventEvent = aSelectedEvent;
    }

    @Override
    public GameScene getEditingObject() {
        return gameScene;
    }

    @Override
    public void processKeyPressedEvent(KeyEvent aKeyEvent) {
        gameScene.getRuntime().getEventManager().fire(new KeyPressedGameEvent(GameKeyCode.valueOf(aKeyEvent.getCode().name())));
    }

    @Override
    public void processKeyReleasedEvent(KeyEvent aKeyEvent) {
        gameScene.getRuntime().getEventManager().fire(new KeyReleasedGameEvent(GameKeyCode.valueOf(aKeyEvent.getCode().name())));
    }

    @Override
    public Node getView() {
        return view;
    }

    @Override
    public void addedAsTab() {
        gameView.startTimer();
        gameLoopThread.start();
    }

    @Override
    public void removed() {
        gameView.stopTimer();
        gameLoopThread.interrupt();
    }

    @Override
    public void onObjectSelected(ObjectSelectedEvent aEvent) {
        gameView.onObjectSelected(aEvent);
    }

    @Override
    public void onShutdown(ShutdownEvent aEvent) {
        gameView.stopTimer();
        gameLoopThread.interrupt();
    }

    public void onMouseClicked(MouseEvent aEvent) {
        Position theClickPosition = new Position(aEvent.getX(), aEvent.getY());
        for (GameObjectInstance theInstance : cameraComponent.getObjectsToDrawInRightOrder(gameScene)) {
            if (theInstance.contains(theClickPosition)) {
                objectSelectedEventEvent.fire(new ObjectSelectedEvent(theInstance));
            }
        }
    }
}