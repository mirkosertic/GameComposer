package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.process.AbstractGameProcess;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

public class FollowCameraProcess extends AbstractGameProcess {

    private final GameObjectInstance cameraObject;
    private final GameObjectInstance playerObject;

    FollowCameraProcess(GameObjectInstance aCameraObject, GameObjectInstance aPlayerInstance) {
        cameraObject = aCameraObject;
        playerObject = aPlayerInstance;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        CameraBehavior theCameraComponent = cameraObject.getComponent(CameraBehavior.class);
        Position theCameraPosition = cameraObject.positionProperty().get();
        Size theCameraSize = theCameraComponent.getScreenSize();

        Position theCameraCenter = new Position(theCameraPosition.x + theCameraSize.width / 2, theCameraPosition.y + theCameraSize.height / 2);

        Position thePlayerPosition = playerObject.positionProperty().get();
        Size thePlayerSize = playerObject.getOwnerGameObject().sizeProperty().get();

        Position thePlayerCenter = new Position(thePlayerPosition.x + thePlayerSize.width / 2, thePlayerPosition.y + thePlayerSize.height / 2);

        float theDX = thePlayerCenter.x - theCameraCenter.x;
        float theDY = thePlayerCenter.y - theCameraCenter.y;

        float theMX = theDX / 300 * aElapsedTimeSinceLastLoop;
        float theMY = theDY / 300 * aElapsedTimeSinceLastLoop;

        cameraObject.positionProperty().set(new Position(theCameraPosition.x + theMX, theCameraPosition.y + theMY));

        return ProceedResult.CONTINUE_RUNNING;
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        String theInstanceUID = aInstance.uuidProperty().get();
        return cameraObject.uuidProperty().get().equals(theInstanceUID)
                || playerObject.uuidProperty().get().equals(theInstanceUID);
    }
}
