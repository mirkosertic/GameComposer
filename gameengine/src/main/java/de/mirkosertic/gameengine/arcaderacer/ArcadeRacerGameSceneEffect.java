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

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectType;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Color;
import de.mirkosertic.gameengine.type.EffectCanvas;
import de.mirkosertic.gameengine.type.ResourceName;
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;

public class ArcadeRacerGameSceneEffect implements GameSceneEffect {

    private static final ArcadeRacerGameSceneEffectClassInformation CIINSTANCE = new ArcadeRacerGameSceneEffectClassInformation();

    public static final String TYPE_VALUE = "ArcadeRacerGameSceneEffect";

    public static final String SPEED_PROPERTY = "speed";

    public static final String POSITIONONTRACK_PROPERTY = "positionOnTrack";
    public static final String FIELDOFVIEW_PROPERTY = "fieldOfView";
    public static final String VIEWDEPTH_PROPERTY = "viewDepth";
    public static final String CAMERAHEIGHT_PROPERTY = "cameraHeight";
    public static final String DISTANCECAMERA_PROPERTY = "distanceCamera";

    private final GameScene gameScene;
    private Size screenSize;
    private long gameTime;

    private final Property<Double> positionOnTrack;
    private final Property<Integer> fieldOfView;
    private final Property<Integer> viewDepth;
    private final Property<Integer> distanceCamera;
    private final Property<Double> cameraHeight;
    private final Property<Double> speed;

    private final Track track;

    public ArcadeRacerGameSceneEffect(GameScene aScene,
            GameEventManager aEventManager) {
        gameScene = aScene;

        track = new Track();

        speed = new Property<>(Double.class, this, SPEED_PROPERTY, 6d, aEventManager);

        positionOnTrack = new Property<>(Double.class, this, POSITIONONTRACK_PROPERTY, 0d, aEventManager);
        fieldOfView = new Property<>(Integer.class, this, FIELDOFVIEW_PROPERTY, 95, aEventManager);
        viewDepth = new Property<>(Integer.class, this, VIEWDEPTH_PROPERTY, 60, aEventManager);
        cameraHeight = new Property<>(Double.class, this, CAMERAHEIGHT_PROPERTY, 2d, aEventManager);
        distanceCamera = new Property<>(Integer.class, this, DISTANCECAMERA_PROPERTY, -3, aEventManager);

        aEventManager.register(this, SetScreenResolution.TYPE, new GameEventListener<SetScreenResolution>() {
            @Override
            public void handleGameEvent(SetScreenResolution aEvent) {
                screenSize = aEvent.screenSize;
            }
        });
        aEventManager.fire(new StartProcess(new ArcadeRacerGameSceneProcess(this)));
    }

    @Override
    public GameSceneEffectType getEffectType() {
        return GameSceneEffectType.PREPROCESSOR;
    }

    @Override
    public ClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveField
    public Property<Double> positionOnTrack() {
        return positionOnTrack;
    }

    @ReflectiveField
    public Property<Double> cameraHeight() {
        return cameraHeight;
    }

    @ReflectiveField
    public Property<Double> speed() {
        return speed;
    }

    @ReflectiveField
    public Property<Integer> fieldOfView() {
        return fieldOfView;
    }

    @ReflectiveField
    public Property<Integer> viewDepth() {
        return viewDepth;
    }

    @ReflectiveField
    public Property<Integer> distanceCamera() {
        return distanceCamera;
    }

    @ReflectiveField
    public Property<Double> getSpeed() {
        return speed;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        return theResult;
    }

    @Override
    public void render(final EffectCanvas aEffectCanvas, CameraBehavior aCameraBehavior) {

        int theOldTrackDataX[] = null;
        int theOldTrackDataY[] = null;

        double thePositonOnTrack = positionOnTrack.get();

        int theDistanceCamera = distanceCamera.get();
        double theCameraHeight = cameraHeight.get();
        int theNearestZ = (int) thePositonOnTrack + 1;
        int theFarestZ = theNearestZ + viewDepth.get();

        // We have to calculate the camera position properly
        Point3D theCameraPosition;
        if (thePositonOnTrack % 0 == 0) {
            TrackElement theTrackElement = track.getTrackElementForPosition(theNearestZ - theDistanceCamera);
            theCameraPosition = new Point3D(0, -theCameraHeight + theTrackElement.height , -thePositonOnTrack);
        } else {
            TrackElement theTrackElement1 = track.getTrackElementForPosition(theNearestZ - theDistanceCamera);
            TrackElement theTrackElement2 = track.getTrackElementForPosition(theNearestZ - theDistanceCamera + 1);
            double theHeightDelta = theTrackElement2.height - theTrackElement1.height;
            double thePositionModulo = thePositonOnTrack - (int) thePositonOnTrack;
            theCameraPosition = new Point3D(0, -theCameraHeight + theTrackElement1.height + theHeightDelta * thePositionModulo, -thePositonOnTrack);
        }

        Camera theCamera = new Camera(screenSize, theCameraPosition, fieldOfView.get());

        // Calculate the curve data
        int theXoffset = 0;
        for (int theZ = theFarestZ; theZ >= theNearestZ; theZ--) {
            TrackElement theTrackElement = track.getTrackElementForPosition(theZ);
            theXoffset+= theTrackElement.curveFactor;
        }

        // Now we draw the track
        for (int theZ = theFarestZ; theZ >= theNearestZ; theZ--) {

            final String theElementIdentifier = "te" + theZ + "_";

            TrackElement theTrackElement = track.getTrackElementForPosition(theZ);
            double theAngle = Math.toRadians(theTrackElement.angle);

            double theSinAngle = Math.sin(theAngle);
            double theCosAngle = Math.cos(theAngle);

            double theSceneZ = -theZ;
            double theSceneHeight = theTrackElement.height;

            if (theOldTrackDataX == null) {

                theOldTrackDataX = new int[theTrackElement.segments.length * 2];
                theOldTrackDataY = new int[theTrackElement.segments.length * 2];
                for (int i = 0;i<theTrackElement.segments.length;i++) {
                    Segment theSegment = theTrackElement.segments[i];
                    Point2D theSegmentStart  = theCamera.project(theSegment.xStart, theSceneHeight + theSegment.yStart, theSceneZ);
                    Point2D theSegmentEnd  = theCamera.project(theSegment.xEnd, theSceneHeight + theSegment.yEnd, theSceneZ);
                    theOldTrackDataX[i * 2] = theSegmentStart.x + theXoffset;
                    theOldTrackDataY[i * 2] = theSegmentStart.y;

                    theOldTrackDataX[i * 2 + 1] = theSegmentEnd.x + theXoffset;
                    theOldTrackDataY[i * 2 + 1] = theSegmentEnd.y;
                }

            } else {

                int[] theNewTrackDataX = new int[theTrackElement.segments.length * 2];
                int[] theNewTrackDataY = new int[theTrackElement.segments.length * 2];
                for (int i = 0;i<theTrackElement.segments.length;i++) {
                    Segment theSegment = theTrackElement.segments[i];

                    double theStartX = theSegment.xStart * theCosAngle - theSegment.yStart * theSinAngle;
                    double theStartY = theSegment.xStart * theSinAngle + theSegment.yStart * theCosAngle;

                    double theEndX = theSegment.xEnd * theCosAngle - theSegment.yEnd * theSinAngle;
                    double theEndY = theSegment.xEnd * theSinAngle + theSegment.yEnd * theCosAngle;

                    Point2D theSegmentStart  = theCamera.project(theStartX, theSceneHeight + theStartY, theSceneZ);
                    Point2D theSegmentEnd  = theCamera.project(theEndX, theSceneHeight + theEndY, theSceneZ);

                    theNewTrackDataX[i * 2] = theSegmentStart.x + theXoffset;
                    theNewTrackDataY[i * 2] = theSegmentStart.y;
                    theNewTrackDataX[i * 2 + 1] = theSegmentEnd.x + theXoffset;
                    theNewTrackDataY[i * 2 + 1] = theSegmentEnd.y;

                    // Backface culling
                    if (theSegment.color != null) {
                        // Color
                        aEffectCanvas.fillRectangle(theElementIdentifier + "t_" + i,
                                theOldTrackDataX[i*2], theOldTrackDataY[i*2],
                                theOldTrackDataX[i*2+1], theOldTrackDataY[i*2+1],
                                theNewTrackDataX[i*2+1], theNewTrackDataY[i*2+1],
                                theNewTrackDataX[i*2], theNewTrackDataY[i*2],
                                theSegment.color, theZ);
                    } else {
                        // Texture
                        final int theFinalOldTrackDataX[] = theOldTrackDataX;
                        final int theFinalOldTrackDataY[] = theOldTrackDataY;

                        final int theFinalI = i;
                        final int theFinalZ = theZ;

                        gameScene.getRuntime().getResourceCache().getResourceFor(theSegment.texture).thenContinue(new Promise.NoReturnHandler<GameResource>() {
                            @Override
                            public void process(GameResource aResult) {
                                aEffectCanvas.fillRectangle(theElementIdentifier + "t_" + theFinalI, aResult,
                                        theFinalOldTrackDataX[theFinalI*2], theFinalOldTrackDataY[theFinalI*2],
                                        theFinalOldTrackDataX[theFinalI*2+1], theFinalOldTrackDataY[theFinalI*2+1],
                                        theFinalOldTrackDataX[theFinalI*2+1], theFinalOldTrackDataY[theFinalI*2+1],
                                        theFinalOldTrackDataX[theFinalI*2], theFinalOldTrackDataY[theFinalI*2],
                                        0, 0, 1, 0, 1, 1, 0, 1, theFinalZ);
                            }
                        }).catchError(new Promise.ErrorHandler<String>() {
                            @Override
                            public void process(String aResult, Exception aOptionalRejectedException) {
                                aEffectCanvas.fillRectangle(theElementIdentifier + "t_" + theFinalI,
                                        theFinalOldTrackDataX[theFinalI*2], theFinalOldTrackDataY[theFinalI*2],
                                        theFinalOldTrackDataX[theFinalI*2+1], theFinalOldTrackDataY[theFinalI*2+1],
                                        theFinalOldTrackDataX[theFinalI*2+1], theFinalOldTrackDataX[theFinalI*2+1],
                                        theFinalOldTrackDataX[theFinalI*2], theFinalOldTrackDataX[theFinalI*2],
                                        Color.WHITE, theFinalZ);
                            }
                        });
                    }
                }

                // Keep track of the old positions to prevent them fro being recomputed
                theOldTrackDataX = theNewTrackDataX;
                theOldTrackDataY = theNewTrackDataY;
            }

            // Draw the Sprites on the Track
            for (int i=0;i<theTrackElement.sprites.length;i++) {
                Sprite theSprite = theTrackElement.sprites[i];

                double theTopLeftX = (theSprite.positionX - theSprite.width / 2) * theCosAngle - (theSceneHeight - theSprite.height) * theSinAngle;
                double theTopLeftY = (theSprite.positionX - theSprite.width / 2) * theSinAngle + (theSceneHeight - theSprite.height) * theCosAngle;

                double theTopRightX = (theSprite.positionX + theSprite.width / 2) * theCosAngle - (theSceneHeight - theSprite.height) * theSinAngle;
                double theTopRightY = (theSprite.positionX + theSprite.width / 2) * theSinAngle + (theSceneHeight - theSprite.height) * theCosAngle;

                double theBottomLeftX = (theSprite.positionX - theSprite.width / 2) * theCosAngle - (theSceneHeight) * theSinAngle;
                double theBottomLeftY = (theSprite.positionX - theSprite.width / 2) * theSinAngle + (theSceneHeight) * theCosAngle;

                double theBottomRightX = (theSprite.positionX + theSprite.width / 2) * theCosAngle - (theSceneHeight) * theSinAngle;
                double theBottomRightY = (theSprite.positionX + theSprite.width / 2) * theSinAngle + (theSceneHeight) * theCosAngle;

                final Point2D theTopLeft  = theCamera.project(theTopLeftX, theTopLeftY , theSceneZ);
                final Point2D theTopRight  = theCamera.project(theTopRightX, theTopRightY , theSceneZ);
                final Point2D theBottomLeft  = theCamera.project(theBottomLeftX, theBottomLeftY , theSceneZ);
                final Point2D theBottomRight  = theCamera.project(theBottomRightX, theBottomRightY , theSceneZ);

                if (theBottomRight.x >=0 && theTopLeft.x < screenSize.width) {
                    //
                    ResourceName theResourceName = theSprite.computeCurrentView(gameTime);
                    final int theFinalXOffset = theXoffset;
                    final int theFinalZ = theZ;
                    final int theFinalI = i;

                    gameScene.getRuntime().getResourceCache().getResourceFor(theResourceName).thenContinue(new Promise.NoReturnHandler<GameResource>() {
                        @Override
                        public void process(GameResource aResult) {
                            aEffectCanvas.fillRectangle(theElementIdentifier + "s_" + theFinalI, aResult,
                                    theTopLeft.x + theFinalXOffset, theTopLeft.y, theTopRight.x + theFinalXOffset, theTopRight.y, theBottomRight.x + theFinalXOffset, theBottomRight.y,theBottomLeft.x + theFinalXOffset, theBottomLeft.y,
                                    0, 0, 1, 0, 1, 1, 0, 1, theFinalZ - 2);
                        }
                    }).catchError(new Promise.ErrorHandler<String>() {
                        @Override
                        public void process(String aResult, Exception aOptionalRejectedException) {
                            throw new RuntimeException(aResult, aOptionalRejectedException);
                        }
                    });
                }
            }

            theXoffset -= theTrackElement.curveFactor;
        }
    }

    @Override
    public GameScene getScene() {
        return gameScene;
    }

    public static ArcadeRacerGameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData) {
        ArcadeRacerGameSceneEffect theResult = new ArcadeRacerGameSceneEffect(aScene, aGameRuntime.getEventManager());
        return theResult;
    }

    public GameProcess.ProceedResult proceesGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        positionOnTrack.set(positionOnTrack.get() + speed.get() / 1000 * aElapsedTimeSinceLastLoop);
        gameTime = aGameTime;
        return GameProcess.ProceedResult.CONTINUE_RUNNING;
    }

    @Override
    public void delete() {
        gameScene.removeEffect(this);
    }
}