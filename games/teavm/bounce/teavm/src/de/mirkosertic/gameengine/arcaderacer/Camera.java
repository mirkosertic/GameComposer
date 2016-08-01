package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Size;

public class Camera {

    private final int halfViewWidth;
    private final int halfViewHeight;
    private final Point3D position;
    private final double d;

    public Camera(Size aSceeenSize, Point3D aCameraPosition, int aFOV) {
        halfViewWidth = aSceeenSize.width / 2;
        halfViewHeight = aSceeenSize.height / 2;
        position = aCameraPosition;
        d = 1 / Math.tan(Math.toRadians(aFOV / 2));
    }

    public Point2D project(double aX, double aY, double aZ) {

        double theXCameraSpace = aX - position.x;
        double theYCameraSpace = aY - position.y;
        double theZCameraSpace = aZ - position.z;

        double theXProjected = theXCameraSpace * d / theZCameraSpace;
        double theYProjected = theYCameraSpace * d  / theZCameraSpace;

        int x = (int) (halfViewWidth - (halfViewWidth * theXProjected));
        int y = (int) (halfViewHeight - (halfViewHeight * theYProjected));

        return new Point2D(x, y);
    }
}
