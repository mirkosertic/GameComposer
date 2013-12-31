package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraComponent extends GameComponent implements Camera {

    static final String TYPE = "CameraComponent";

    private final GameObjectInstance objectInstance;
    private final Property<CameraType> type;

    private Size screenSize;

    CameraComponent(GameObjectInstance aObjectInstance) {
        this(aObjectInstance, aObjectInstance.getOwnerGameObject().getComponentTemplate(CameraComponentTemplate.class));
    }

    CameraComponent(GameObjectInstance aObjectInstance, CameraComponentTemplate aTemplate) {
        objectInstance = aObjectInstance;

        GameEventManager theEventManager = aObjectInstance.getOwnerGameObject().getGameScene().getRuntime().getEventManager();

        type = registerProperty(new Property<CameraType>(this, "type", aTemplate.typeProperty().get(), theEventManager));
    }

    void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SetScreenResolution.class,
                new GameEventListener<SetScreenResolution>() {
                    public void handleGameEvent(SetScreenResolution aEvent) {
                        setScreenSize(new Size(aEvent.screenSize.width, aEvent.screenSize.height));
                    }
                });
    }

    public GameObjectInstance getObjectInstance() {
        return objectInstance;
    }

    public Property<CameraType> typeProperty() {
        return type;
    }

    public Size getScreenSize() {
        return screenSize;
    }

    void setScreenSize(Size screenSize) {
        this.screenSize = screenSize;
    }

    public List<GameObjectInstance> getObjectsToDrawInRightOrder(GameScene aScene) {
        // TODO: Implement Z-Ordering here
        List<GameObjectInstance> theResult = new ArrayList<GameObjectInstance>();

        Size theScreenSize = getScreenSize();
        if (theScreenSize != null) {
            Position theCameraPosition = objectInstance.positionProperty().get();
            for (GameObjectInstance theInstance : aScene.getInstances()) {
                // Just visible instances need to be drawn
                if (theInstance.visibleProperty().get()) {
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
            return theAnchor.compute(aInstance.positionProperty().get(), screenSize);
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
    public String getTypeKey() {
        return TYPE;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    @Override
    public CameraComponentTemplate getTemplate() {
        return objectInstance.getOwnerGameObject().getComponentTemplate(CameraComponentTemplate.class);
    }

    public static CameraComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new CameraComponent(aObjectInstance);
    }

    public void centerOn(GameObjectInstance aGameObjectInstance) {
        Position theObjectPosition = aGameObjectInstance.positionProperty().get();
        Size theObjectSize = aGameObjectInstance.getOwnerGameObject().sizeProperty().get();

        float theCenterX = theObjectPosition.x + theObjectSize.width / 2;
        float theCenterY = theObjectPosition.y + theObjectSize.height / 2;

        objectInstance.positionProperty().set(
                new Position(theCenterX - screenSize.width / 2, theCenterY - screenSize.height / 2));
    }
}
