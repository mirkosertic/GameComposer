package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.contentarea.ContentChildController;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.javafx.JavaFXGameView;
import javafx.scene.Node;
import javafx.scene.input.KeyEvent;

public class GameSceneEditorController implements ContentChildController<GameScene> {

    private GameScene gameScene;
    private Node view;
    private JavaFXGameView gameView;
    private Thread gameLoopThread;

    GameSceneEditorController(GameScene aScene, Node aView, JavaFXGameView aGameView, Thread aGameLoopThread) {
        gameScene = aScene;
        view = aView;
        gameView = aGameView;
        gameLoopThread = aGameLoopThread;
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
}