/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.android;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.Game;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GestureDetector;
import de.mirkosertic.gameengine.core.PlaySceneStrategy;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.TouchIdentifier;
import de.mirkosertic.gameengine.type.TouchPosition;

public class GameEngineActivity extends Activity {

    private AndroidGameSoundSystemFactory gameSoundSystemFactory;
    private final AndroidGameRuntimeFactory gameRuntimeFactory;
    private final GameLoopFactory gameLoopFactory;
    private AndroidCanvas androidCanvas;
    private NetworkConnector networkConnector;

    private Game game;
    private PlaySceneStrategy playSceneStrategy;

    public GameEngineActivity() {

        networkConnector = new AndroidNetworkConnector();

        gameRuntimeFactory = new AndroidGameRuntimeFactory();
        gameLoopFactory = new GameLoopFactory();

        playSceneStrategy = new PlaySceneStrategy(gameRuntimeFactory, gameLoopFactory, networkConnector) {

            private AndroidGameView gameView;

            @Override
            protected void loadOtherScene(String aSceneId) {
                loadScene(aSceneId);
            }

            @Override
            protected Size getScreenSize() {
                return new Size(androidCanvas.getWidth(), androidCanvas.getHeight());
            }

            @Override
            protected GestureDetector createGestureDetectorFor(GameEventManager aEventManager, CameraBehavior aCamera) {
                return new AndroidGestureDetector(aEventManager, aCamera);
            }

            @Override
            protected AndroidGameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector) {
                if (gameView == null) {
                    gameView = new AndroidGameView(androidCanvas, aCamera, aGameRuntime, aGestureDetector);
                } else {
                    gameView.prepareNewScene(aGameRuntime, aCamera, aGestureDetector);
                }
                gameView.setCurrentScreenSize(getScreenSize());
                return gameView;
            }

            @Override
            public void handleResize() {
                Size theCurrentSize = getScreenSize();
                if ((theCurrentSize != null) && (theCurrentSize.width != 0) && (theCurrentSize.height != 0)) {
                    getRunningGameLoop().getScene().getRuntime().getEventManager().fire(new SetScreenResolution(theCurrentSize));
                    if (gameView != null) {
                        gameView.setCurrentScreenSize(theCurrentSize);
                    }
                }
            }
        };
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
        androidCanvas.setPlaySceneStrategy(playSceneStrategy);
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
        if (playSceneStrategy.hasGameLoop()) {
            GestureDetector theGestureDetector = playSceneStrategy.getRunningGameLoop().getHumanGameView().getGestureDetector();

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
        InputStream theStream = null;
        try {
            theStream = theAssetManager.open("game.json");
            JSONObject theGameJSON = new JSONObject(IOUtils.toString(theStream));
            Map<String, Object> theGameData = JSONUtils.toMap(theGameJSON);

            game = Game.deserialize(theGameData);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(theStream);
        }

        String theDefaultScene = game.defaultSceneProperty().get();
        loadScene(theDefaultScene);
    }

    private static void safeClose(InputStream aStream) {
        if (aStream != null) {
            try {
                aStream.close();
            } catch (IOException e) {
            }
        }
    }

    private void loadScene(String aSceneID) {
        AssetManager theAssetManager = getAssets();

        InputStream theStream = null;
        try {
            theStream = theAssetManager.open(aSceneID + "/scene.json");
            JSONObject theSceneJSON = new JSONObject(IOUtils.toString(theStream));
            Map<String, Object> theGameData = JSONUtils.toMap(theSceneJSON);

            AndroidGameResourceLoader theResourceLoader = new AndroidGameResourceLoader(theAssetManager, getCacheDir(), aSceneID);

            GameRuntime theRuntime = gameRuntimeFactory.create(theResourceLoader, gameSoundSystemFactory);
            GameScene theGameScene = GameScene.deserialize(game, theRuntime, theGameData);

            playScene(theGameScene);

        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        } finally {
            safeClose(theStream);
        }
    }

    private void playScene(GameScene aGameScene) {
        playSceneStrategy.playScene(aGameScene).thenContinue(new Promise.NoReturnHandler<GameScene>() {
            @Override
            public void process(GameScene aResult) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu aMenu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, aMenu);
        return true;
    }
}