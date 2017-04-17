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
package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gamecomposer.FXMLLoaderFactory;
import de.mirkosertic.gamecomposer.ObjectSelectedEvent;
import de.mirkosertic.gamecomposer.contentarea.ContentAreaFactory;
import de.mirkosertic.gamecomposer.contentarea.ContentAreaFactoryType;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.GameLoop;
import de.mirkosertic.gameengine.core.GameLoopFactory;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSystem;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.input.DefaultGestureDetector;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.type.Size;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;
import java.util.ResourceBundle;
import javax.enterprise.event.Event;
import javax.inject.Inject;

@ContentAreaFactoryType(clazz = GameScene.class)
public class GameSceneEditorControllerFactory implements ContentAreaFactory<GameScene, GameSceneEditorController> {

    @Inject
    Event<ObjectSelectedEvent> objectSelectedEventEvent;

    @Inject
    FXMLLoaderFactory fxmlLoaderFactory;

    @Override
    public GameSceneEditorController create(GameScene aScene) {

        final GameEventManager theEventManager = aScene.getRuntime().getEventManager();
        GameRuntime theRuntime = aScene.getRuntime();

        GameObject theDefaultCamera = aScene.cameraObjectProperty().get();
        if (theDefaultCamera == null) {
            throw new IllegalArgumentException("No camera set");
        }

        // Detect and create a camera
        GameObjectInstance theCameraInstance = aScene.createFrom(theDefaultCamera);
        CameraBehavior theCameraBehavior = theCameraInstance.getBehavior(CameraBehavior.TYPE);
        if (theCameraBehavior == null) {
            throw new IllegalArgumentException("No camera component in camera object");
        }

        GamePhysicsManager thePhysicsManager = null;
        for (GameSystem theSystem : theRuntime.getSystems()) {
            if (theSystem instanceof GamePhysicsManager) {
                thePhysicsManager = (GamePhysicsManager) theSystem;
            }
        }

        EditorFXGameView theGameView = new EditorFXGameView(theRuntime, theCameraBehavior, new DefaultGestureDetector(theEventManager, theCameraBehavior), thePhysicsManager);

        GameLoopFactory theGameLoopFactory = new GameLoopFactory();
        GameLoop theMainLoop = theGameLoopFactory.create(aScene, theGameView, theRuntime);

        final CameraBehavior theFinalCameraComponent = theCameraBehavior;

        // Set defaults, this will be overridden
        theEventManager.fire(new SetScreenResolution(new Size(200, 200)));

        try (InputStream fxml = GameSceneEditorController.class.getResourceAsStream("GameSceneEditor.fxml")) {
            FXMLLoader theLoader = fxmlLoaderFactory.createLoader();
            ResourceBundle theBundle = ResourceBundle.getBundle("de.mirkosertic.gamecomposer.contentarea.gamescene.GameSceneEditor");
            theLoader.setResources(theBundle);
            BorderPane root = theLoader.load(fxml);

            GameSceneEditorController theController = theLoader.getController();
            theController.centerBorderPane.widthProperty().addListener((observableValue, number, number2) -> theEventManager.fire(new SetScreenResolution(new Size((int) ((double) number2), theFinalCameraComponent.getScreenSize().height))));
            theController.centerBorderPane.heightProperty().addListener((observableValue, number, number2) -> theEventManager.fire(new SetScreenResolution(new Size(theFinalCameraComponent.getScreenSize().width, (int) ((double) number2)))));
            theGameView.getCanvasNode().widthProperty().bind(theController.centerBorderPane.widthProperty());
            theGameView.getCanvasNode().heightProperty().bind(theController.centerBorderPane.heightProperty());

            return theController.initialize(theRuntime, aScene, root, theGameView, theMainLoop, theCameraBehavior, objectSelectedEventEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
