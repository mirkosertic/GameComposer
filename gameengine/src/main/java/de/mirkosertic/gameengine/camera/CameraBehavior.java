package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.Behavior;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraBehavior implements Behavior, Camera, Reflectable<CameraClassInformation> {

    static final String TYPE = "Camera";

    private final GameObjectInstance objectInstance;
    private final Property<CameraType> type;

    private Size screenSize;

    CameraBehavior(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getBehaviorTemplate(CameraBehaviorTemplate.class));
    }

    CameraBehavior(GameObjectInstance aObjectInstance, CameraBehaviorTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        type = new Property<>(CameraType.class, this, TYPE_PROPERTY, aTemplate.typeProperty().get(), theEventManager);
    }

    void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SetScreenResolution.class,
                new GameEventListener<SetScreenResolution>() {
                    @Override
                    public void handleGameEvent(SetScreenResolution aEvent) {
                        setScreenSize(new Size(aEvent.screenSize.width, aEvent.screenSize.height));
                    }
                });
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public CameraClassInformation getClassInformation() {
        return CameraClassInformation.INSTANCE;
    }

    public GameObjectInstance getObjectInstance() {
        return objectInstance;
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

    public List<GameObjectInstance> getObjectsToDrawInRightOrder(GameScene aScene) {
        // TODO: Implement Z-Ordering here
        List<GameObjectInstance> theResult = new ArrayList<>();

        Size theScreenSize = getScreenSize();
        if (theScreenSize != null) {
            Position theCameraPosition = objectInstance.positionProperty().get();
            for (GameObjectInstance theInstance : aScene.getInstances()) {
                if (theInstance == objectInstance) {
                    // The camera object itself does not need to be drawn
                    continue;
                }
                // Just visible instances need to be drawn
                if (theInstance.visibleProperty().get()) {
                    //TODO: Optimize position handling here
                    if (theInstance.absolutePositionProperty().get()) {
                        theResult.add(theInstance);
                    } else {
                        Position theInstancePosition = theInstance.positionProperty().get();
                        Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();
                        if (theInstancePosition.x + theSize.width >= theCameraPosition.x
                                && theInstancePosition.x <= theCameraPosition.x + theScreenSize.width
                                && theInstancePosition.y + theSize.height >= theCameraPosition.y
                                && theInstancePosition.y <= theCameraPosition.y + theScreenSize.height) {
                            theResult.add(theInstance);
                        }
                    }
                }
            }
        }
        return theResult;
    }

    public Position transformToScreenPosition(GameObjectInstance aInstance) {
        if (aInstance.absolutePositionProperty().get()) {
            AbsolutePositionAnchor theAnchor = aInstance.absolutePositionAnchorProperty().get();
            if (theAnchor != null) {
                return theAnchor.compute(aInstance.positionProperty().get(), screenSize);
            } else {
                throw new IllegalStateException("instance " + aInstance.nameProperty().get()+" is set to invalid absolute position anchor");
            }
        }
        return transformToScreenPosition(aInstance.positionProperty().get());
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
        return objectInstance.getOwnerGameObject().getBehaviorTemplate(CameraBehaviorTemplate.class);
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
}