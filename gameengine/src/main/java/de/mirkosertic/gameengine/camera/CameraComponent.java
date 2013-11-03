package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.*;

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
            Position theCameraPosition = objectInstance.getPosition();

            for (GameObjectInstance theInstance : aScene.getInstances()) {
                Position theInstancePosition = theInstance.getPosition();
                Size theInstanceSize = theInstance.getSize();
                if (theInstancePosition.getX() + theInstanceSize.getWidth() >= theCameraPosition.getX() && theInstancePosition.getX() <= theCameraPosition.getX() + theScreenSize.getWidth() &&
                        theInstancePosition.getY() + theInstanceSize.getHeight() >= theCameraPosition.getY() && theInstancePosition.getY() <= theCameraPosition.getY() + theScreenSize.getHeight()) {
                    theResult.add(theInstance);
                }
            }
        }
        return theResult;
    }

    public Position transformToScreenPosition(Position aWorldPosition) {

        Position theCameraPosition = objectInstance.getPosition();

        return new Position(aWorldPosition.getX() - theCameraPosition.getX(), aWorldPosition.getY() - theCameraPosition.getY());
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

    public static CameraComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new CameraComponent(aObjectInstance);
    }
}
