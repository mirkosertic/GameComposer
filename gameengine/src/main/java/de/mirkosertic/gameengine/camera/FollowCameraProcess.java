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

        Position theCameraCenter = new Position(theCameraPosition.x + theCameraSize.width / 2, theCameraPosition.y + theCameraSize.height / 2);

        Position thePlayerPosition = playerObject.getPosition();
        Size thePlayerSize = playerObject.getOwnerGameObject().getSize();

        Position thePlayerCenter = new Position(thePlayerPosition.x + thePlayerSize.width / 2, thePlayerPosition.y + thePlayerSize.height / 2);

        float theDX = thePlayerCenter.x - theCameraCenter.x;
        float theDY = thePlayerCenter.y - theCameraCenter.y;

        float theMX = theDX / 300 * aElapsedTimeSinceLastLoop;
        float theMY = theDY / 300 * aElapsedTimeSinceLastLoop;

        cameraObject.setPosition(new Position(theCameraPosition.x + theMX, theCameraPosition.y + theMY));
    }
}
