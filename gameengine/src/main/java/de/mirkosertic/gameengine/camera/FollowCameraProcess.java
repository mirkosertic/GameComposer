package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.Size;
import de.mirkosertic.gameengine.processes.GameProcess;

public class FollowCameraProcess implements GameProcess {

    private GameObjectInstance cameraObject;
    private GameObjectInstance playerObject;

    public FollowCameraProcess(GameObjectInstance aCameraObject, GameObjectInstance aPlayerInstance) {
        cameraObject = aCameraObject;
        playerObject = aPlayerInstance;
    }

    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        CameraComponent theCameraComponent = cameraObject.getComponent(CameraComponent.class);
        Position theCameraPosition = cameraObject.getPosition();
        Size theCameraSize = theCameraComponent.getScreenSize();

        Position theCameraCenter = new Position(theCameraPosition.getX() + theCameraSize.getWidth() / 2, theCameraPosition.getY() + theCameraSize.getHeight() / 2);

        Position thePlayerPosition = playerObject.getPosition();
        Size thePlayerSize = playerObject.getSize();

        Position thePlayerCenter = new Position(thePlayerPosition.getX() + thePlayerSize.getWidth() / 2, thePlayerPosition.getY() + thePlayerSize.getHeight() / 2);

        float theDX = thePlayerCenter.getX() - theCameraCenter.getX();
        float theDY = thePlayerCenter.getY() - theCameraCenter.getY();

        float theMX = theDX / 300 * aElapsedTimeSinceLastLoop;
        float theMY = theDY / 300 * aElapsedTimeSinceLastLoop;

        cameraObject.setPosition(new Position(theCameraPosition.getX() + theMX, theCameraPosition.getY() + theMY));
    }
}
