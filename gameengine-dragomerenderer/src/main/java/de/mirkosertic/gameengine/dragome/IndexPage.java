package de.mirkosertic.gameengine.dragome;

import org.w3c.dom.Element;
import org.w3c.dom.events.Event;
import org.w3c.dom.events.EventListener;
import org.w3c.dom.events.KeyboardEvent;
import org.w3c.dom.events.MouseEvent;

import com.dragome.annotations.PageAlias;
import com.dragome.enhancers.jsdelegate.JsDelegateFactory;
import com.dragome.html.dom.html5canvas.HTMLCanvasElement;
import com.dragome.services.ServiceLocator;
import com.dragome.view.DefaultVisualActivity;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.type.GameKeyCode;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

@PageAlias(alias= "index")
public class IndexPage extends DefaultVisualActivity {

    private GameLoopFactory gameLoopFactory;
    private DragomeRuntimeFactory runtimeFactory;
    private DragomeGameSceneLoader sceneLoader;
    private Window window;
    private PlaySceneStrategy playSceneStrategy;
    private Game game;

    @Override
    public void build() {

        window = new Window();

        gameLoopFactory = new GameLoopFactory();
        runtimeFactory = new DragomeRuntimeFactory();

        sceneLoader = new DragomeGameSceneLoader(new DragomeGameSceneLoader.GameSceneLoadedListener() {
            @Override
            public void onGameSceneLoaded(GameScene aScene) {
                DragomeLogger.info("Loaded game scene " + aScene.nameProperty().get());
                playScene(aScene);
            }

            @Override
            public void onGameSceneLoadedError() {
                DragomeLogger.error("Failed to load scene");
            }
        }, runtimeFactory);

        DragomeGameLoader theLoader = new DragomeGameLoader(new DragomeGameLoader.GameLoadedListener() {
            @Override
            public void onGameLoaded(Game aGame) {
                game = aGame;
                String theSceneId = aGame.defaultSceneProperty().get();
                DragomeLogger.info("Loading game scene " + theSceneId);
                sceneLoader.loadFromServer(aGame, aGame.defaultSceneProperty().get(), new DragomeGameResourceLoader(theSceneId));
            }

            @Override
            public void onGameLoadedError() {
                DragomeLogger.error("Failed to load game.");
            }
        });

        playSceneStrategy = new PlaySceneStrategy(runtimeFactory, gameLoopFactory) {

            private DragomeGameView gameView;

            @Override
            protected void loadOtherScene(String aSceneId) {
                sceneLoader.loadFromServer(game, aSceneId, new DragomeGameResourceLoader(aSceneId));
            }

            @Override
            protected Size getScreenSize() {
                return new Size(window.getClientWidth(), window.getClientHeight());
            }

            @Override
            protected GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                if (gameView == null) {
                    Element elementBySelector= ServiceLocator.getInstance().getDomHandler().getElementBySelector("#html5canvas");
                    HTMLCanvasElement theCanvas= JsDelegateFactory.createFromNode(elementBySelector, HTMLCanvasElement.class);

                    gameView = new DragomeGameView(aGameRuntime, aCamera, aGestureDetector, theCanvas);
                } else {
                    gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                }
                gameView.setCurrentScreenSize(getScreenSize());
                return gameView;
            }

            @Override
            public void handleResize() {
                Size theSize = getScreenSize();
                getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theSize));
                gameView.setCurrentScreenSize(theSize);
            }
        };

        DragomeLogger.info("Loading game...");
        theLoader.loadFromServer();

        EventListener theGlobalEventListener = new EventListener() {
            @Override
            public void handleEvent(Event aEvent) {
                if (aEvent instanceof KeyboardEvent) {
                    handleSingleKeyboardEvent((KeyboardEvent) aEvent);
                }
                if (aEvent instanceof MouseEvent) {
                    handleSingleMouseEvent((MouseEvent) aEvent);
                }
            }
        };

        window.onResize(new Runnable() {
            @Override
            public void run() {
                if (playSceneStrategy.hasGameLoop()) {
                    playSceneStrategy.handleResize();
                }
            }
        });

        window.addEventListener(theGlobalEventListener, "keydown", "keypress", "keyup", "mousedown", "mouseup");
    }

    private void handleSingleMouseEvent(MouseEvent aMouseEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            if ("mousedown".equals(aMouseEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mousePressed(
                        new Position(aMouseEvent.getClientX(), aMouseEvent.getClientY())
                );
            }
            if ("mouseup".equals(aMouseEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().mouseReleased(
                        new Position(aMouseEvent.getClientX(), aMouseEvent.getClientY())
                );
            }
        }
    }

    private void handleSingleKeyboardEvent(KeyboardEvent aKeyboardEvent) {
        if (playSceneStrategy.hasGameLoop()) {
            GameKeyCode theKeyCode = DragomeKeyCodeTranslator.translate(aKeyboardEvent.getKeyIdentifier());
            if ("keyup".equals(aKeyboardEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyReleased(theKeyCode);
            }
            if ("keydown".equals(aKeyboardEvent.getType())) {
                playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector().keyPressed(theKeyCode);
            }
        }
        DragomeLogger.info("KeyEvent " + aKeyboardEvent.getType()+" "+ aKeyboardEvent.getKeyIdentifier());
    }

    private void runSingleStep(final GameLoop aGameLoop) {
        if (!aGameLoop.isShutdown()) {
            aGameLoop.singleRun();
            window.requestAnimationFrame(new Runnable() {
                @Override
                public void run() {
                    runSingleStep(aGameLoop);
                }
            });
        }
    }

    private void playScene(GameScene aGameScene) {
        playSceneStrategy.playScene(aGameScene);
        runSingleStep(playSceneStrategy.getRunningGameLoop());

        DragomeLogger.info("Scene initialized");
    }
}