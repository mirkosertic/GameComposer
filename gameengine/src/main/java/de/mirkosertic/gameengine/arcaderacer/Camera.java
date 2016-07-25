package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.Size;

public class Camera {

    private final int halfViewWidth;
    private final int halfViewHeight;
    private final double distanceProjectionPlane;
    private final double positionOnTrack;

    public Camera(Size aSceeenSize, double aDistanceProjectionPlane,
            double aPositionOnTrack) {
        halfViewWidth = aSceeenSize.width / 2;
        halfViewHeight = aSceeenSize.height / 2;
        distanceProjectionPlane = aDistanceProjectionPlane;
        positionOnTrack = aPositionOnTrack;
    }

    public Point2D project(Point3D aPoint) {

        double theProjectedDistance = aPoint.z - positionOnTrack;

        double theYFactor = aPoint.y * distanceProjectionPlane / theProjectedDistance;
        double theXFactor = aPoint.x * distanceProjectionPlane / theProjectedDistance;

        int x = (int) (halfViewWidth + (halfViewWidth * theXFactor));
        int y = (int) (halfViewHeight + (halfViewHeight * theYFactor));

        return new Point2D(x, y);
    }
}
