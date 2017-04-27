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
package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.PositionAnchor;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraBehavior implements Behavior, Camera, Reflectable<CameraClassInformation> {

    private static final CameraClassInformation CIINSTANCE = new CameraClassInformation();

    public static final BehaviorType TYPE = new BehaviorType("Camera");

    private final GameObjectInstance objectInstance;
    private final Property<CameraType> type;
    private final GameScene scene;

    private Size screenSize;

    CameraBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, (CameraBehaviorTemplate) aObjectInstance.getOwnerGameObject().getBehaviorTemplate(CameraBehaviorTemplate.TYPE));
    }

    CameraBehavior(GameObjectInstance aObjectInstance, CameraBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;
        scene = objectInstance.getOwnerGameObject().getGameScene();

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        type = new Property<>(CameraType.class, this, TYPE_PROPERTY, aTemplate.typeProperty().get(), theEventManager);
    }

    void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SetScreenResolution.TYPE,
                new GameEventListener<SetScreenResolution>() {
                    @Override
                    public void handleGameEvent(SetScreenResolution aEvent) {
                        setScreenSize(new Size(aEvent.screenSize.width, aEvent.screenSize.height));
                    }
                });
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public CameraClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public Property<CameraType> typeProperty() {
        return type;
    }

    public Size getScreenSize() {
        return screenSize;
    }

    void setScreenSize(Size screenSize) {
        this.screenSize = screenSize;
        switch (getTemplate().typeProperty().get()) {
            case CENTERONSCENE:
                // Recompute the center during resize
                centerOnScene();
                break;
        }
    }

    public int processVisibleInstances(Callback aCallback) {

        int theCounter = 0;

        Size theScreenSize = getScreenSize();
        if (theScreenSize != null) {
            Position theCameraPosition = objectInstance.positionProperty().get();

            for (GameObjectInstance theInstance : scene.getInstances()) {
                if (theInstance == objectInstance) {
                    // The camera object itself does not need to be drawn
                    continue;
                }
                // Just visible instances need to be drawn
                if (aCallback.accepts(theInstance)) {
                    PositionAnchor theAnchor = theInstance.positionAnchorProperty().get();
                    Position theScreenPosition = theAnchor.compute(theInstance.positionProperty().get(), theCameraPosition, screenSize);
                    Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();
                    // Do some clipping to prevent invisible objects from beeing rendered
                    if (theScreenPosition.x + theSize.width >= 0
                            && theScreenPosition.x <= theScreenSize.width
                            && theScreenPosition.y + theSize.height >= 0
                            && theScreenPosition.y <= theScreenSize.height) {
                        aCallback.process(theInstance, theScreenPosition, theSize);
                        theCounter++;
                    }
                }
            }
        }

        return theCounter;
    }

    public Position transformToScreenPosition(Position aWorldPosition) {
        Position theCameraPosition = objectInstance.positionProperty().get();
        return new Position(aWorldPosition.x - theCameraPosition.x, aWorldPosition.y - theCameraPosition.y);
    }

    public Position transformFromScreen(Position aScreenPosition) {
        Position theCameraPosition = objectInstance.positionProperty().get();
        return new Position(theCameraPosition.x + aScreenPosition.x, theCameraPosition.y + aScreenPosition.y);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public CameraBehaviorTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(CameraBehaviorTemplate.TYPE);
    }

    @Override
    public void markAsRemoteObject() {
    }

    public static CameraBehavior deserialize(GameObjectInstance aObjectInstance) {
        return new CameraBehavior(aObjectInstance);
    }

    private void centerOn(GameObjectInstance aGameObjectInstance) {
        Position theObjectPosition = aGameObjectInstance.positionProperty().get();
        Size theObjectSize = aGameObjectInstance.getOwnerGameObject().sizeProperty().get();

        float theCenterX = theObjectPosition.x + theObjectSize.width / 2;
        float theCenterY = theObjectPosition.y + theObjectSize.height / 2;

        centerOn(new Position(theCenterX, theCenterY));
    }

    private void centerOn(Position aPosition) {
        objectInstance.positionProperty().set(
                new Position(aPosition.x - screenSize.width / 2, aPosition.y - screenSize.height / 2));
    }

    public void initializeFor(GameScene aGameScene, GameObjectInstance aPlayerInstance) {
        switch (getTemplate().typeProperty().get()) {
            case FOLLOWPLAYER:
                centerOn(aPlayerInstance);
                aGameScene.getRuntime().getEventManager().fire(new StartProcess(new FollowCameraProcess(objectInstance, aPlayerInstance)));
                break;
            case CENTERONSCENE:
                centerOnScene();
                break;
        }
    }

    public void centerOnScene() {
        Position thePosition = objectInstance.getOwnerGameObject().getGameScene().computeCenter();
        centerOn(thePosition);
    }

    @Override
    public void delete() {
        objectInstance.getOwnerGameObject().getGameScene().removeBehaviorFrom(objectInstance.getOwnerGameObject(), this);
    }

    @Override
    public GameObjectInstance getInstance() {
        return objectInstance;
    }

    public GameObjectInstance[] findInstancesAt(final Position aScreenPosition, final boolean aIncludeOnlyVisible) {
        final List<GameObjectInstance> theInstances = new ArrayList<>();
        processVisibleInstances(new Callback() {

            @Override
            public boolean accepts(GameObjectInstance aInstance) {
                if (aIncludeOnlyVisible) {
                    return aInstance.visibleProperty().get();
                }
                return true;
            }

            @Override
            public void process(GameObjectInstance aValue, Position aPositionOnScreen, Size aSize) {
                if (aPositionOnScreen.x <= aScreenPosition.x &&
                        aPositionOnScreen.y <= aScreenPosition.y &&
                        aPositionOnScreen.x + aSize.width >= aScreenPosition.x &&
                        aPositionOnScreen.y + aSize.height >= aScreenPosition.y) {
                    theInstances.add(aValue);
                }
            }
        });
        return theInstances.toArray(new GameObjectInstance[theInstances.size()]);
    }
}