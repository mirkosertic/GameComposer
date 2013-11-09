package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CameraComponent implements GameComponent {

    public static final String TYPE = "CameraComponent";

    private GameObjectInstance objectInstance;

    private Size screenSize;

    CameraComponent(GameObjectInstance aObjectInstance) {
        objectInstance = aObjectInstance;
    }

    void registerEvents(GameRuntime aGameRuntime) {
        aGameRuntime.getEventManager().register(objectInstance, SetScreenResolutionEvent.class, new GameEventListener<SetScreenResolutionEvent>() {
            public void handleGameEvent(SetScreenResolutionEvent aEvent) {
                setScreenSize(new Size(aEvent.getScreenWidth(), aEvent.getScreenHeight()));
            }
        });
    }

    public GameObjectInstance getObjectInstance() {
        return objectInstance;
    }

    public Size getScreenSize() {
        return screenSize;
    }

    public void setScreenSize(Size screenSize) {
        this.screenSize = screenSize;
    }


    public List<GameObjectInstance> getObjectsToDrawInRightOrder(GameScene aScene) {
        //TODO: Implement Z-Ordering here
        List<GameObjectInstance> theResult = new ArrayList<GameObjectInstance>();

        Size theScreenSize = getScreenSize();
        if (theScreenSize != null) {
            Position theCameraPosition = objectInstance.positionProperty().get();

            for (GameObjectInstance theInstance : aScene.getInstances()) {
                Position theInstancePosition = theInstance.positionProperty().get();
                Size theSize = theInstance.getOwnerGameObject().sizeProperty().get();
                if (theInstancePosition.x + theSize.width >= theCameraPosition.x && theInstancePosition.x <= theCameraPosition.x + theScreenSize.width &&
                        theInstancePosition.y + theSize.height >= theCameraPosition.y && theInstancePosition.y <= theCameraPosition.y + theScreenSize.height) {
                    theResult.add(theInstance);
                }
            }
        }
        return theResult;
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

        objectInstance.positionProperty().set(new Position(theCenterX - screenSize.width / 2, theCenterY - screenSize.height / 2));
    }
}
