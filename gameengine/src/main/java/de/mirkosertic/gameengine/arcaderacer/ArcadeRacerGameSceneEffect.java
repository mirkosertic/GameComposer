package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.GameProcess;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.type.*;

import java.io.IOException;
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

        aEventManager.register(this, SetScreenResolution.class, new GameEventListener<SetScreenResolution>() {
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
    public void render(EffectCanvas aEffectCanvas, CameraBehavior aCameraBehavior) {

        aEffectCanvas.setPaint(Color.BLACK);
        aEffectCanvas.fillRect(0, 0, screenSize.width, screenSize.height);

        int theOldTrackDataX[] = null;
        int theOldTrackDataY[] = null;

        int theOldCurbX[] = null;
        int theOldCurbY[] = null;

        double thePositonOnTrack = positionOnTrack.get();

        int theDistanceCamera = distanceCamera.get();
        double theCameraHeight = cameraHeight.get();
        int theNearestZ = (int) thePositonOnTrack;
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
            Road theRoad = theTrackElement.road;
            theXoffset+= theRoad.curveFactor;
        }

        // Now we draw the track
        for (int theZ = theFarestZ; theZ >= theNearestZ; theZ--) {

            TrackElement theTrackElement = track.getTrackElementForPosition(theZ);
            Road theRoad = theTrackElement.road;

            double theSceneZ = -theZ;
            double theSceneHeight = theTrackElement.height;

            Point2D theRoadLeftOutside  = theCamera.project(theRoad.positionLeft, theSceneHeight, theSceneZ);
            Point2D theRoadRightOutside  = theCamera.project(theRoad.positionRight, theSceneHeight, theSceneZ);

            if (theOldTrackDataX == null) {
                theOldTrackDataX = new int[] {theRoadLeftOutside.x + theXoffset, theRoadRightOutside.x + theXoffset};
                theOldTrackDataY = new int[] {theRoadLeftOutside.y, theRoadRightOutside.y};

                theOldCurbX = new int[theRoad.curbs.length * 2];
                theOldCurbY = new int[theRoad.curbs.length * 2];
                for (int i=0;i<theRoad.curbs.length;i++) {
                    Curb theCurb = theRoad.curbs[i];
                    Point2D theCurbLeft  = theCamera.project(theCurb.getPosition(), theSceneHeight, theSceneZ);
                    Point2D theCurbRight  = theCamera.project(theCurb.getPosition() + theCurb.getWidth(), theSceneHeight, theSceneZ);

                    theOldCurbX[i*2] = theCurbLeft.x + theXoffset;
                    theOldCurbY[i*2] = theCurbLeft.y;
                    theOldCurbX[i*2 + 1] = theCurbRight.x + theXoffset;
                    theOldCurbY[i*2 + 1] = theCurbRight.y;
                }
            } else {

                int theNewTrackDataX[] = new int[] {theRoadLeftOutside.x + theXoffset, theRoadRightOutside.x + theXoffset};
                int theNewTrackDataY[] = new int[] {theRoadLeftOutside.y, theRoadRightOutside.y};

                int theNewCurbX[] = new int[theRoad.curbs.length * 2];
                int theNewCurbY[] = new int[theRoad.curbs.length * 2];
                for (int i=0;i<theRoad.curbs.length;i++) {
                    Curb theCurb = theRoad.curbs[i];
                    Point2D theCurbLeft  = theCamera.project(theCurb.getPosition(), theSceneHeight, theSceneZ);
                    Point2D theCurbRight  = theCamera.project(theCurb.getPosition() + theCurb.getWidth(), theSceneHeight, theSceneZ);

                    theNewCurbX[i*2] = theCurbLeft.x + theXoffset;
                    theNewCurbY[i*2] = theCurbLeft.y;
                    theNewCurbX[i*2 + 1] = theCurbRight.x + theXoffset;
                    theNewCurbY[i*2 + 1] = theCurbRight.y;
                }

                // Draw things
                // Clip invisible faces
                if (theNewTrackDataY[0] > theOldTrackDataY[0]) {

                    aEffectCanvas.setPaint(theTrackElement.terrainColor);

                    // Left part of the road
                    aEffectCanvas.fillTriangle(0d, theOldTrackDataY[0], theOldTrackDataX[0], theOldTrackDataY[0], theNewTrackDataX[0], theNewTrackDataY[0]);
                    aEffectCanvas.fillTriangle(0d, theNewTrackDataY[0], theNewTrackDataX[0], theNewTrackDataY[0], 0d, theOldTrackDataY[0]);

                    // Right part of the road
                    aEffectCanvas.fillTriangle(theOldTrackDataX[1], theOldTrackDataY[1], screenSize.width, theOldTrackDataY[1], screenSize.width, theNewTrackDataY[1]);
                    aEffectCanvas.fillTriangle(theOldTrackDataX[1], theOldTrackDataY[1],theNewTrackDataX[1], theNewTrackDataY[1], screenSize.width, theNewTrackDataY[1]);

                    // The road itself
                    try {
                        GameResource theRoadTexture = gameScene.getRuntime().getResourceCache().getResourceFor(theRoad.texture);

                        aEffectCanvas
                                .fillTriangle(theRoadTexture, theOldTrackDataX[0], theOldTrackDataY[0], theOldTrackDataX[1], theOldTrackDataY[0],
                                        theNewTrackDataX[1], theNewTrackDataY[0],
                                        0, 0, 511, 0, 511, 511);

                        aEffectCanvas
                                .fillTriangle(theRoadTexture, theNewTrackDataX[0], theNewTrackDataY[0], theOldTrackDataX[0], theOldTrackDataY[0],
                                        theNewTrackDataX[1], theNewTrackDataY[1],
                                        0, 511, 0, 0, 511, 511);

                    } catch (IOException e) {
                        aEffectCanvas.setPaint(theRoad.roadColor);
                        aEffectCanvas
                                .fillTriangle(theOldTrackDataX[0], theOldTrackDataY[0], theOldTrackDataX[1], theOldTrackDataY[0],
                                        theNewTrackDataX[1], theNewTrackDataY[0]);
                        aEffectCanvas
                                .fillTriangle(theNewTrackDataX[0], theNewTrackDataY[0], theOldTrackDataX[0], theOldTrackDataY[0],
                                        theNewTrackDataX[1], theNewTrackDataY[1]);
                    }

                    // And the Curbs
                    for (int i=0;i<theRoad.curbs.length;i++) {
                        Curb theCurb = theRoad.curbs[i];
                        aEffectCanvas.setPaint(theCurb.getColor());

                        aEffectCanvas.fillTriangle(theOldCurbX[i*2], theOldCurbY[i*2], theOldCurbX[i*2+1], theOldCurbY[i*2+1], theNewCurbX[i*2+1], theNewCurbY[i*2+1]);
                        aEffectCanvas.fillTriangle(theNewCurbX[i*2], theNewCurbY[i*2], theOldCurbX[i*2], theOldCurbY[i*2], theNewCurbX[i*2+1], theNewCurbY[i*2+1]);
                    }
                }

                // Keep track of the old positions to prevent them fro being recomputed
                theOldTrackDataX = theNewTrackDataX;
                theOldTrackDataY = theNewTrackDataY;
                theOldCurbX = theNewCurbX;
                theOldCurbY = theNewCurbY;
            }

            // Draw the Sprites on the Track
            for (Sprite theSprite : theTrackElement.sprites) {
                Point2D theTopLeft  = theCamera.project(theSprite.positionX - theSprite.width / 2, theSceneHeight - theSprite.height , theSceneZ);
                Point2D theBottomRight  = theCamera.project(theSprite.positionX + theSprite.width / 2, theSceneHeight, theSceneZ);

                if (theBottomRight.x >=0 && theTopLeft.x < screenSize.width) {
                    //
                    ResourceName theResourceName = theSprite.computeCurrentView(gameTime);
                    try {
                        GameResource theResource = gameScene.getRuntime().getResourceCache().getResourceFor(theResourceName);
                        aEffectCanvas
                                .drawScaled(theResource, theTopLeft.x + theXoffset, theTopLeft.y, theBottomRight.x - theTopLeft.x,
                                        theBottomRight.y - theTopLeft.y);
                    } catch (IOException e) {
                        aEffectCanvas.setPaint(Color.WHITE);
                        aEffectCanvas
                                .fillRect(theTopLeft.x + theXoffset, theTopLeft.y, theBottomRight.x - theTopLeft.x,
                                        theBottomRight.y - theTopLeft.y);
                    }
                }
            }

            theXoffset -= theRoad.curveFactor;
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
}