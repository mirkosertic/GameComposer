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

            String theElementIdentifier = "te" + theZ + "_";

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
                        try {
                            GameResource theTexture = gameScene.getRuntime().getResourceCache().getResourceFor(theSegment.texture);

                            aEffectCanvas.fillRectangle(theElementIdentifier + "t_" + i, theTexture,
                                    theOldTrackDataX[i*2], theOldTrackDataY[i*2],
                                    theOldTrackDataX[i*2+1], theOldTrackDataY[i*2+1],
                                    theNewTrackDataX[i*2+1], theNewTrackDataY[i*2+1],
                                    theNewTrackDataX[i*2], theNewTrackDataY[i*2],
                                    0, 0, 511, 0, 511, 511, 0, 511, theZ);

                        } catch (IOException e) {
                            // Color
                            aEffectCanvas.fillRectangle(theElementIdentifier + "t_" + i,
                                    theOldTrackDataX[i*2], theOldTrackDataY[i*2],
                                    theOldTrackDataX[i*2+1], theOldTrackDataY[i*2+1],
                                    theNewTrackDataX[i*2+1], theNewTrackDataY[i*2+1],
                                    theNewTrackDataX[i*2], theNewTrackDataY[i*2],
                                    Color.WHITE, theZ);
                        }
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

                Point2D theTopLeft  = theCamera.project(theTopLeftX, theTopLeftY , theSceneZ);
                Point2D theTopRight  = theCamera.project(theTopRightX, theTopRightY , theSceneZ);
                Point2D theBottomLeft  = theCamera.project(theBottomLeftX, theBottomLeftY , theSceneZ);
                Point2D theBottomRight  = theCamera.project(theBottomRightX, theBottomRightY , theSceneZ);

                if (theBottomRight.x >=0 && theTopLeft.x < screenSize.width) {
                    //
                    ResourceName theResourceName = theSprite.computeCurrentView(gameTime);
                    try {
                        GameResource theResource = gameScene.getRuntime().getResourceCache().getResourceFor(theResourceName);
                        aEffectCanvas.fillRectangle(theElementIdentifier + "s_" + i, theResource,
                                theTopLeft.x + theXoffset, theTopLeft.y, theTopRight.x + theXoffset, theTopRight.y, theBottomRight.x + theXoffset, theBottomRight.y,theBottomLeft.x + theXoffset, theBottomLeft.y,
                                0, 0, 63, 0, 63, 63, 0, 63, theZ - 2);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
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
}