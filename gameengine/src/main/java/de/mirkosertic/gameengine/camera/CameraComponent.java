package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.*;

import java.util.ArrayList;
import java.util.List;

public class CameraComponent implements GameComponent {

    private GameRuntime gameRuntime;
    private GameObjectInstance objectInstance;
    private Size screenSize;

    CameraComponent(GameObjectInstance aObjectInstance, GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
        objectInstance = aObjectInstance;
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

    public Position transformToScreenPosition(Position aPosition) {

        Position theCameraPosition = objectInstance.getPosition();

        return new Position(aPosition.getX() - theCameraPosition.getX(), aPosition.getY() - theCameraPosition.getY());
    }

    public Size getScreenSize() {
        return screenSize;
    }
}