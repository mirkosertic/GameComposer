package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectType;
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

    private final Property<Float> positionOnTrack;
    private final Property<Integer> fieldOfView;
    private final Property<Integer> viewDepth;
    private final Property<Integer> distanceCamera;
    private final Property<Float> cameraHeight;
    private final Property<Float> speed;

    private final Track track;

    public ArcadeRacerGameSceneEffect(GameScene aScene,
            GameEventManager aEventManager) {
        gameScene = aScene;

        track = new Track();

        speed = new Property<>(Float.class, this, SPEED_PROPERTY, 6f, aEventManager);

        positionOnTrack = new Property<>(Float.class, this, POSITIONONTRACK_PROPERTY, 0f, aEventManager);
        fieldOfView = new Property<>(Integer.class, this, FIELDOFVIEW_PROPERTY, 95, aEventManager);
        viewDepth = new Property<>(Integer.class, this, VIEWDEPTH_PROPERTY, 60, aEventManager);
        cameraHeight = new Property<>(Float.class, this, CAMERAHEIGHT_PROPERTY, 2f, aEventManager);
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
    public Property<Float> positionOnTrack() {
        return positionOnTrack;
    }

    @ReflectiveField
    public Property<Float> cameraHeight() {
        return cameraHeight;
    }

    @ReflectiveField
    public Property<Float> speed() {
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
    public Property<Float> getSpeed() {
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

        float thePositonOnTrack = positionOnTrack.get();

        int theNearestZ = (int) Math.floor(thePositonOnTrack);
        int theFarestZ = theNearestZ + viewDepth.get();

        // We have to calculate the camera position properly
        Point3D theCameraPosition = null;
        if (thePositonOnTrack % 0 == 0) {
            TrackElement theTrackElement = track.getTrackElementForPosition(theNearestZ - distanceCamera.get());
            theCameraPosition = new Point3D(0, -cameraHeight.get() + theTrackElement.height , -thePositonOnTrack);
        } else {
            TrackElement theTrackElement1 = track.getTrackElementForPosition(theNearestZ - distanceCamera.get());
            TrackElement theTrackElement2 = track.getTrackElementForPosition(theNearestZ - distanceCamera.get() + 1);
            double theHeightDelta = theTrackElement2.height - theTrackElement1.height;
            theCameraPosition = new Point3D(0, -cameraHeight.get() + theTrackElement1.height + theHeightDelta * (thePositonOnTrack % 1) , -thePositonOnTrack);
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

            TrackElement theTrackElement = track.getTrackElementForPosition((int) theZ);
            Road theRoad = theTrackElement.road;

            double theSceneZ = -theZ;
            double theSceneHeight = theTrackElement.height;

            Point2D theRoadLeftOutside  = theCamera.project(new Point3D(theRoad.positionLeft, theSceneHeight, theSceneZ));
            Point2D theRoadRightOutside  = theCamera.project(new Point3D(theRoad.positionRight, theSceneHeight, theSceneZ));

            if (theOldTrackDataX == null) {
                theOldTrackDataX = new int[] {theRoadLeftOutside.x + theXoffset, theRoadRightOutside.x + theXoffset};
                theOldTrackDataY = new int[] {theRoadLeftOutside.y, theRoadRightOutside.y};

                theOldCurbX = new int[theRoad.curbs.length * 2];
                theOldCurbY = new int[theRoad.curbs.length * 2];
                for (int i=0;i<theRoad.curbs.length;i++) {
                    Curb theCurb = theRoad.curbs[i];
                    Point2D theCurbLeft  = theCamera.project(new Point3D(theCurb.getPosition(), theSceneHeight, theSceneZ));
                    Point2D theCurbRight  = theCamera.project(new Point3D(theCurb.getPosition() + theCurb.getWidth(), theSceneHeight, theSceneZ));

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
                    Point2D theCurbLeft  = theCamera.project(new Point3D(theCurb.getPosition(), theSceneHeight, theSceneZ));
                    Point2D theCurbRight  = theCamera.project(new Point3D(theCurb.getPosition() + theCurb.getWidth(), theSceneHeight, theSceneZ));

                    theNewCurbX[i*2] = theCurbLeft.x + theXoffset;
                    theNewCurbY[i*2] = theCurbLeft.y;
                    theNewCurbX[i*2 + 1] = theCurbRight.x + theXoffset;
                    theNewCurbY[i*2 + 1] = theCurbRight.y;
                }

                // Draw things
                // Clip invisible faces
                if (theNewTrackDataY[0] > theOldTrackDataY[0]) {

                    aEffectCanvas.setPaint(theTrackElement.terrainColor);

                    // Left and right part of the Road
                    aEffectCanvas.fillPolygon(new float[] { 0, theOldTrackDataX[0], theNewTrackDataX[0], 0 },
                            new float[] { theOldTrackDataY[0], theOldTrackDataY[0], theNewTrackDataY[0], theNewTrackDataY[0] }, 4);
                    aEffectCanvas.fillPolygon(new float[] { theOldTrackDataX[1], screenSize.width, screenSize.width, theNewTrackDataX[1] },
                            new float[] { theOldTrackDataY[1], theOldTrackDataY[1], theNewTrackDataY[1], theNewTrackDataY[1] }, 4);

                    // The road itself
                    aEffectCanvas.setPaint(theRoad.roadColor);
                    aEffectCanvas.fillPolygon(new float[] { theOldTrackDataX[0], theOldTrackDataX[1], theNewTrackDataX[1], theNewTrackDataX[0] },
                            new float[] { theOldTrackDataY[0], theOldTrackDataY[1], theNewTrackDataY[1], theNewTrackDataY[0] }, 4);

                    // And the Curbs
                    for (int i=0;i<theRoad.curbs.length;i++) {
                        Curb theCurb = theRoad.curbs[i];
                        aEffectCanvas.setPaint(theCurb.getColor());
                        aEffectCanvas.fillPolygon(new float[] { theOldCurbX[i*2], theOldCurbX[i*2+1], theNewCurbX[i*2+1], theNewCurbX[i*2]},
                                new float[] { theOldCurbY[i*2], theOldCurbY[i*2+1], theNewCurbY[i*2+1], theNewCurbY[i*2]}, 4);
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
                Point2D theTopLeft  = theCamera.project(new Point3D(theSprite.positionX - theSprite.width / 2, theSceneHeight - theSprite.height , theSceneZ));
                Point2D theBottomRight  = theCamera.project(new Point3D(theSprite.positionX + theSprite.width / 2, theSceneHeight, theSceneZ));

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