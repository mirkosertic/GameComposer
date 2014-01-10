package de.mirkosertic.gameengine.android;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.FollowCameraProcess;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.RunScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;
import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class GameEngineActivity extends Activity {

    private AndroidGameSoundSystemFactory gameSoundSystemFactory;
    private final AndroidGameRuntimeFactory gameRuntimeFactory;
    private final GameLoopFactory gameLoopFactory;
    private AndroidCanvas androidCanvas;

    private Game game;

    private GameLoop runningGameLoop;

    public GameEngineActivity() {
        gameRuntimeFactory = new AndroidGameRuntimeFactory();
        gameLoopFactory = new GameLoopFactory();
    }

    /**
     * Called when the activity is first created.
     *
     * @param aSavedState If the activity is being re-initialized after
     *                    previously being shut down then this Bundle contains the data it most
     *                    recently supplied in onSaveInstanceState(Bundle). <b>Note: Otherwise it is null.</b>
     */
    @Override
    public void onCreate(Bundle aSavedState) {
        super.onCreate(aSavedState);

        gameSoundSystemFactory = new AndroidGameSoundSystemFactory(getAssets());

        setContentView(R.layout.activity_main);

        androidCanvas = (AndroidCanvas) findViewById(R.id.canvasView);
        androidCanvas.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View aView, MotionEvent aEvent) {
                handleTouchEvent(aEvent);
                return true;
            }
        });

        // Load the game, this is done in another thread
        Runnable theRunnable = new Runnable() {
            @Override
            public void run() {
                loadGame();
            }
        };
        new Thread(theRunnable).start();
    }

    private TouchPosition[] toArray(MotionEvent aEvent) {
        TouchPosition[] thePosition = new TouchPosition[aEvent.getPointerCount()];
        for (int i = 0; i < aEvent.getPointerCount(); i++) {
            thePosition[i] = new TouchPosition(new TouchIdentifier(aEvent.getPointerId(i)), (int) aEvent.getX(i), (int) aEvent.getY(i));
        }
        return thePosition;
    }

    private void handleTouchEvent(MotionEvent aEvent) {
        if (runningGameLoop != null) {
            GestureDetector theGestureDetector = runningGameLoop.getHumanGameView().getGestureDetector();

            switch (aEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    theGestureDetector.touchStarted(toArray(aEvent));
                    break;
                case MotionEvent.ACTION_UP:
                    theGestureDetector.touchEnded(toArray(aEvent));
                    break;
                case MotionEvent.ACTION_MOVE:
                    theGestureDetector.touchMoved(toArray(aEvent));
                    break;
                case MotionEvent.ACTION_CANCEL:
                    theGestureDetector.touchCanceled(toArray(aEvent));
                    break;
            }
        }
    }

    private void loadGame() {
        AssetManager theAssetManager = getAssets();
        try (InputStream theStream = theAssetManager.open("game.json")) {
            JSONObject theGameJSON = new JSONObject(IOUtils.toString(theStream));
            Map<String, Object> theGameData = JSONUtils.toMap(theGameJSON);

            game = Game.deserialize(theGameData);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (runningGameLoop != null && !runningGameLoop.isShutdown()) {
                    runningGameLoop.singleRun();
                }
            }
        }, 0, 16);

        String theDefaultScene = game.defaultSceneProperty().get();
        loadScene(theDefaultScene);
    }

    private void loadScene(String aSceneID) {
        AssetManager theAssetManager = getAssets();

        try (InputStream theStream = theAssetManager.open(aSceneID + "/scene.json")) {
            JSONObject theSceneJSON = new JSONObject(IOUtils.toString(theStream));
            Map<String, Object> theGameData = JSONUtils.toMap(theSceneJSON);

            AndroidGameResourceLoader theResourceLoader = new AndroidGameResourceLoader(getAssets(), aSceneID);

            GameRuntime theRuntime = gameRuntimeFactory.create(theResourceLoader, gameSoundSystemFactory);
            GameScene theGameScene = GameScene.deserialize(theRuntime, theGameData);

            playScene(theGameScene);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void playScene(GameScene aGameScene) {

        if (runningGameLoop != null) {
            runningGameLoop.shutdown();
        }

        GameRuntime theRuntime = aGameScene.getRuntime();
        GameEventManager theEventManager = theRuntime.getEventManager();

        gameRuntimeFactory.loadingFinished(aGameScene);

        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
        CameraBehavior theCameraComponent = theCameraObjectInstance.getComponent(CameraBehavior.class);

        GameObjectInstance thePlayerInstance = null;
        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
            if (theInstance.getOwnerGameObject() == aGameScene.defaultPlayerProperty().get()) {
                thePlayerInstance = theInstance;
            }
        }

        // This is our hook to load new scenes
        theEventManager.register(null, RunScene.class, new GameEventListener<RunScene>() {
            @Override
            public void handleGameEvent(RunScene aEvent) {
                String theSceneId = aEvent.sceneId;
                loadScene(theSceneId);
            }
        });

        AndroidGameView theGameView = new AndroidGameView(androidCanvas, theCameraComponent, theRuntime);

        GameLoop theLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

        theEventManager.fire(new SetScreenResolution(new Size(androidCanvas.getWidth(), androidCanvas.getHeight())));

        runningGameLoop = theLoop;

        switch (theCameraComponent.getTemplate().typeProperty().get()) {
            case FOLLOWPLAYER:
                theCameraComponent.centerOn(thePlayerInstance);
                theEventManager.fire(new StartProcess(new FollowCameraProcess(theCameraObjectInstance, thePlayerInstance)));
                break;
            case CENTERONSCENE:
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, aMenu);
        return true;
    }
}