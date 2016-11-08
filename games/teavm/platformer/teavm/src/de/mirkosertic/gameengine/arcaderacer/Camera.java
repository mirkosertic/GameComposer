/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
