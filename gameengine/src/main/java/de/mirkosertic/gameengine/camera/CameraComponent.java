package de.mirkosertic.gameengine.camera;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.mirkosertic.gameengine.core.GameComponent;
import de.mirkosertic.gameengine.core.GameEventListener;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.SetScreenResolutionEvent;
import de.mirkosertic.gameengine.core.Size;

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
                setScreenResolution(aEvent.getScreenWidth(), aEvent.getScreenHeight());
            }
        });
    }

    void setScreenResolution(int aScreenWidth, int aScremHeight) {
        screenSize = new Size(aScreenWidth, aScremHeight);
    }

    public List<GameObjectInstance> getObjectsToDrawInRightOrder(GameScene aScene) {
        //TODO: Implement Z-Ordering here

        Position theCameraPosition = objectInstance.getPosition();

        List<GameObjectInstance> theResult = new ArrayList<GameObjectInstance>();
        for (GameObjectInstance theInstance : aScene.getInstances()) {
            Position theInstancePosition = theInstance.getPosition();
            Size theInstanceSize = theInstance.getSize();
            if (theInstancePosition.getX() + theInstanceSize.getWidth() >= theCameraPosition.getX() && theInstancePosition.getX() <= theCameraPosition.getX() + screenSize.getWidth() &&
                    theInstancePosition.getY() + theInstanceSize.getHeight() >= theCameraPosition.getY() && theInstancePosition.getY() <= theCameraPosition.getY() + screenSize.getHeight()) {
                theResult.add(theInstance);
            }
        }
        return theResult;
    }

    public Position transformToScreenPosition(Position aWorldPosition) {

        Position theCameraPosition = objectInstance.getPosition();

        return new Position(aWorldPosition.getX() - theCameraPosition.getX(), aWorldPosition.getY() - theCameraPosition.getY());
    }

    public Size getScreenSize() {
        return screenSize;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE);
        return theResult;
    }

    public static CameraComponent deserialize(GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        return new CameraComponent(aObjectInstance);
    }
}
