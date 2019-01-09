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
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.AbstractGameRuntimeFactory;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.SystemException;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.network.DefaultEventInterpreter;
import de.mirkosertic.gameengine.network.EventInterpreter;
import de.mirkosertic.gameengine.network.NetworkConnector;
import de.mirkosertic.gameengine.network.NetworkGameView;
import de.mirkosertic.gameengine.network.NetworkGameViewFactory;
import de.mirkosertic.gameengine.network.NewGameInstance;
import de.mirkosertic.gameengine.type.Size;
import de.mirkosertic.gameengine.type.UUID;

public abstract class PlaySceneStrategy {

    private GameLoop runningGameLoop;

    private final AbstractGameRuntimeFactory runtimeFactory;
    private final GameLoopFactory gameLoopFactory;
    private final NetworkConnector networkConnector;

    protected PlaySceneStrategy(AbstractGameRuntimeFactory aRuntimeFactory, GameLoopFactory aGameLoopFactory, NetworkConnector aNetworkConnector) {
        runtimeFactory = aRuntimeFactory;
        gameLoopFactory = aGameLoopFactory;
        networkConnector = aNetworkConnector;
    }

    public boolean hasGameLoop() {
        return runningGameLoop != null;
    }

    public GameLoop getRunningGameLoop() {
        return runningGameLoop;
    }

    protected abstract void loadOtherScene(String aSceneId);

    protected abstract Size getScreenSize();

    protected abstract GameView getOrCreateCurrentGameView(GameRuntime aGameRuntime, CameraBehavior aCamera, GestureDetector aGestureDetector);

    public abstract void handleResize();

    protected void loadingFinished(GameScene aGameScene) {
        runtimeFactory.loadingFinished(aGameScene);
    }

    protected GestureDetector createGestureDetectorFor(GameEventManager aEventManager, CameraBehavior aCamera) {
        return new DefaultGestureDetector(aEventManager, aCamera);
    }

    protected EventInterpreter createEventInterpreter() {
        return new DefaultEventInterpreter();
    }

    protected void handleSystemException(SystemException e) {
    }

    public Promise<GameScene, String> playScene(final GameScene aGameScene) {
        return new Promise<>(new Promise.Executor() {
            @Override
            public void process(final PromiseResolver aResolver, PromiseRejector aRejector) {

                final GameRuntime theRuntime = aGameScene.getRuntime();
                final GameEventManager theEventManager = theRuntime.getEventManager();

                if (runningGameLoop != null) {
                    theRuntime.getLogger().info("Shutting down previous game loop");
                    runningGameLoop.shutdown();
                }

                theRuntime.getLogger().info("Registering global exception handler");

                theEventManager.register(null, SystemException.TYPE, new GameEventListener<SystemException>() {
                    @Override
                    public void handleGameEvent(SystemException aEvent) {
                        theRuntime.getLogger().error("Fatal system exception : " + aEvent.exception.getMessage());
                        handleSystemException(aEvent);
                    }
                } );

                final Runnable theFinishedRunnable = new Runnable() {
                    @Override
                    public void run() {

                        theRuntime.getLogger().info("Continuing with loading after ressources are cached");

                        loadingFinished(aGameScene);

                        theRuntime.getLogger().info("Starting with camera and player init");

                        GameObject theCameraObject = aGameScene.cameraObjectProperty().get();
                        GameObjectInstance theCameraObjectInstance = aGameScene.createFrom(theCameraObject);
                        CameraBehavior theCameraBehavior = theCameraObjectInstance.getBehavior(CameraBehavior.TYPE);

                        GameObjectInstance thePlayerInstance = null;
                        for (GameObjectInstance theInstance : aGameScene.getInstances()) {
                            if (theInstance.getOwnerGameObject() == aGameScene.defaultPlayerProperty().get()) {
                                thePlayerInstance = theInstance;
                            }
                        }

                        // If there is a networked game
                        // we need unique player instance ids
                        // After loading they are the same on every instance
                        if (thePlayerInstance != null) {
                            thePlayerInstance.uuidProperty().set(UUID.randomUID());
                        }

                        // This is our hook to load new scenes
                        theEventManager.register(null, RunScene.TYPE, new GameEventListener<RunScene>() {
                            @Override
                            public void handleGameEvent(RunScene aEvent) {
                                loadOtherScene(aEvent.sceneId);
                            }
                        });

                        GestureDetector theGestureDetector = createGestureDetectorFor(theRuntime.getEventManager(), theCameraBehavior);
                        GameView theGameView = getOrCreateCurrentGameView(theRuntime, theCameraBehavior, theGestureDetector);

                        theRuntime.getLogger().info("Creating new event loop");
                        GameLoop theLoop = gameLoopFactory.create(aGameScene, theGameView, theRuntime);

                        Size theScreenResolution = getScreenSize();
                        theEventManager.fire(new SetScreenResolution(theScreenResolution));

                        runningGameLoop = theLoop;

                        theRuntime.getLogger().info("Initializing camera for new player and scene");
                        theCameraBehavior.initializeFor(aGameScene, thePlayerInstance);

                        // Now initialize the networking
                        EventInterpreter theInterpreter = createEventInterpreter();

                        final NetworkGameViewFactory theNetworkFactory = new NetworkGameViewFactory(networkConnector, theInterpreter);
                        final NetworkGameView theNetworkGameView = theNetworkFactory.createNetworkViewFor(theEventManager);

                        runningGameLoop.addGameView(theNetworkGameView);

                        // Finally notify the other game instances that there is a new player on the field
                        // This event will we sent to the other game instances
                        // And will trigger there a creation of the new remote player
                        theNetworkGameView.handleGameEvent(new NewGameInstance(thePlayerInstance));

                        if (thePlayerInstance != null) {

                            final GameObjectInstance theFinalPlayer = thePlayerInstance;

                            theEventManager.register(null, NewGameInstance.TYPE, new GameEventListener<NewGameInstance>() {
                                @Override
                                public void handleGameEvent(NewGameInstance aEvent) {
                                    // Inform the other instances about the current player
                                    theNetworkGameView.handleGameEvent(new GameObjectInstanceAddedToScene(theFinalPlayer));
                                }
                            });
                        }

                        theRuntime.getLogger().info("Finished with scene init");

                        aResolver.resolve(aGameScene);
                    }
                };

                final Spritesheet[] theSheets = aGameScene.getSpriteSheets();
                if (theSheets.length > 0) {

                    Promise<Spritesheet, String>[] thePromises = new Promise[theSheets.length];
                    for (int i=0;i<theSheets.length;i++) {

                        theRuntime.getLogger().info("Loading sprite sheet " + i + " " + theSheets[i].jsonFileProperty().get().get());

                        thePromises[i] = theRuntime.getResourceCache().loadIntoCache(theSheets[i]);
                    }

                    Promise.all(thePromises).thenContinue(new Promise.NoReturnHandler<Promise[]>() {
                        @Override
                        public void process(Promise[] aResult) {
                            theRuntime.getLogger().info("spritesheet loading finished");

                            theFinishedRunnable.run();
                        }
                    });

                } else {

                    theRuntime.getLogger().info("Continuing loading without spritesheets");

                    theFinishedRunnable.run();
                }
            }
        });
    }
}