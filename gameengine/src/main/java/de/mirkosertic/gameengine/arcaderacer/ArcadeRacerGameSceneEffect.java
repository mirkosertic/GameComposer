package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.camera.SetScreenResolution;
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
import de.mirkosertic.gameengine.type.Size;

import java.util.HashMap;
import java.util.Map;

public class ArcadeRacerGameSceneEffect implements GameSceneEffect {

    private static final ArcadeRacerGameSceneEffectClassInformation CIINSTANCE = new ArcadeRacerGameSceneEffectClassInformation();

    public static final String TYPE_VALUE = "ArcadeRacerGameSceneEffect";

    public static final String TRACK_COLOR_DARK_PROPERTY = "trackColorDark";
    public static final String TRACK_COLOR_LIGHT_PROPERTY = "trackColorLight";
    public static final String ROAD_COLOR_DARK_PROPERTY = "roadColorDark";
    public static final String ROAD_COLOR_LIGHT_PROPERTY = "roadColorLight";
    public static final String CURB_COLOR_DARK_PROPERTY = "curbColorDark";
    public static final String CURB_COLOR_LIGHT_PROPERTY = "curbColorLight";
    public static final String SPEED_PROPERTY = "speed";

    public static final String POSITIONONTRACK_PROPERTY = "positionOnTrack";
    public static final String DISTANCEPROJECTIONPLANE_PROPERTY = "distanceProjectionPlane";
    public static final String DISTANCECAR_PROPERTY = "distanceCar";
    public static final String CAMERAHEIGHT_PROPERTY = "cameraHeight";

    private final GameScene gameScene;
    private Size screenSize;

    private final Property<Color> trackColorDark;
    private final Property<Color> trackColorLight;
    private final Property<Color> roadColorDark;
    private final Property<Color> roadColorLight;
    private final Property<Color> curbColorDark;
    private final Property<Color> curbColorLight;
    private final Property<Float> positionOnTrack;
    private final Property<Float> distanceProjectionPlane;
    private final Property<Float> distanceCar;
    private final Property<Float> cameraHeight;
    private final Property<Float> speed;

    private final Track track;

    public ArcadeRacerGameSceneEffect(GameScene aScene,
            GameEventManager aEventManager) {
        gameScene = aScene;

        track = new Track();

        trackColorDark = new Property<>(Color.class, this, TRACK_COLOR_DARK_PROPERTY, new Color(0, 200, 0), aEventManager);
        trackColorLight = new Property<>(Color.class, this, TRACK_COLOR_LIGHT_PROPERTY, new Color(0, 220, 0), aEventManager);
        roadColorDark = new Property<>(Color.class, this, ROAD_COLOR_DARK_PROPERTY, new Color(160, 160, 160), aEventManager);
        roadColorLight = new Property<>(Color.class, this, ROAD_COLOR_LIGHT_PROPERTY, new Color(180, 180, 180), aEventManager);
        curbColorDark = new Property<>(Color.class, this, CURB_COLOR_DARK_PROPERTY, new Color(255, 0, 0), aEventManager);
        curbColorLight = new Property<>(Color.class, this, CURB_COLOR_LIGHT_PROPERTY, new Color(255, 255, 255), aEventManager);
        speed = new Property<>(Float.class, this, SPEED_PROPERTY, 1f, aEventManager);

        positionOnTrack = new Property<>(Float.class, this, POSITIONONTRACK_PROPERTY, 0f, aEventManager);
        distanceProjectionPlane = new Property<>(Float.class, this, DISTANCEPROJECTIONPLANE_PROPERTY, 3f, aEventManager);
        distanceCar = new Property<>(Float.class, this, DISTANCECAR_PROPERTY, 9f, aEventManager);
        cameraHeight = new Property<>(Float.class, this, CAMERAHEIGHT_PROPERTY, 2f, aEventManager);

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
    public Property<Color> trackColorDark() {
        return trackColorDark;
    }

    @ReflectiveField
    public Property<Color> trackColorLight() {
        return trackColorLight;
    }

    @ReflectiveField
    public Property<Float> positionOnTrack() {
        return positionOnTrack;
    }

    @ReflectiveField
    public Property<Float> distanceProjectionPlane() {
        return distanceProjectionPlane;
    }

    @ReflectiveField
    public Property<Float> distanceCar() {
        return distanceCar;
    }

    @ReflectiveField
    public Property<Float> cameraHeight() {
        return cameraHeight;
    }

    @ReflectiveField
    public Property<Color> curbColorDark() {
        return curbColorDark;
    }

    @ReflectiveField
    public Property<Color> curbColorLight() {
        return curbColorLight;
    }

    @ReflectiveField
    public Property<Float> speed() {
        return speed;
    }

    @ReflectiveField
    public Property<Color> roadColorDark() {
        return roadColorDark;
    }

    @ReflectiveField
    public Property<Color> roadColorLight() {
        return roadColorLight;
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

        float thePositionOnTrack = positionOnTrack.get();

        Camera theCamera = new Camera(screenSize, distanceProjectionPlane.get(),
                positionOnTrack.get());

        int theViewDepth = 60;

        // Ok, first of all we need to compute the track to draw
        int theFarestPosition = (int) Math.ceil(thePositionOnTrack + theViewDepth);
        int theNearestPosition = theFarestPosition - theViewDepth - 1;

        int[] theOldDataX = null;
        int[] theOldDataY = null;

        // Calculate the curve data
        // And the camera position
        int theXoffset = 0;
        for (int theZ = theFarestPosition; theZ >= theNearestPosition; theZ--) {
            TrackElement theTrackElement = track.getTrackElementForPosition(theZ);
            Road theRoad = theTrackElement.getRoad();

            theXoffset+= theRoad.getCurveFactor();
        }

        // Correct some flickery
        TrackElement theNearestElement = track.getTrackElementForPosition((int) theNearestPosition);
        theXoffset+=theNearestElement.getRoad().getCurveFactor() * (1 - thePositionOnTrack % 1);

        int theViewDistance = -9;

        //TrackElement theNearestElementOnCamera = track.getTrackElementForPosition((int) theNearestPosition + theViewDistance);
        //TrackElement theSecondNearestElement = track.getTrackElementForPosition((int) theNearestPosition + theViewDistance + 1);
        //float theDeltaHeight = theSecondNearestElement.getHeight() - theNearestElementOnCamera.getHeight();

        //float theHeightOffset = cameraHeight.get() + theNearestElementOnCamera.getHeight() - theDeltaHeight * (1 - thePositionOnTrack % 1);
        float theHeightOffset = cameraHeight.get();

        // Now we draw the track
        for (float theZ = theFarestPosition; theZ >= theNearestPosition; theZ--) {

            TrackElement theTrackElement = track.getTrackElementForPosition((int) theZ);
            Road theRoad = theTrackElement.getRoad();

            float theRelativeHeight = theHeightOffset + theTrackElement.getHeight();
            float theRelatveZ = theZ;

            Point2D theRoadLeftOutside  = theCamera.project(new Point3D(theRoad.getPositionLeft(), theRelativeHeight, theRelatveZ * 2));
            Point2D theRoadLeftInside  = theCamera.project(new Point3D(theRoad.getPositionLeft() + theRoad.getCurbWidth(), theRelativeHeight, theRelatveZ * 2));
            Point2D theRoadRightInside  = theCamera.project(new Point3D(theRoad.getPositionRight() - theRoad.getCurbWidth(), theRelativeHeight, theRelatveZ * 2));
            Point2D theRoadRightOutside  = theCamera.project(new Point3D(theRoad.getPositionRight(), theRelativeHeight, theRelatveZ * 2));

            if (theOldDataX == null) {
                theOldDataX = new int[] {theRoadLeftOutside.x + theXoffset, theRoadLeftInside.x + theXoffset, theRoadRightInside.x + theXoffset, theRoadRightOutside.x + theXoffset};
                theOldDataY = new int[] {theRoadLeftOutside.y, theRoadLeftInside.y, theRoadRightInside.y, theRoadRightOutside.y};
            } else {

                int tempNewXData[] = new int[] {theRoadLeftOutside.x + theXoffset, theRoadLeftInside.x + theXoffset, theRoadRightInside.x + theXoffset, theRoadRightOutside.x + theXoffset};
                int tempNewYData[] = new int[] {theRoadLeftOutside.y, theRoadLeftInside.y, theRoadRightInside.y, theRoadRightOutside.y};

                // Draw things

                // Calculate the color according the the real track data
                aEffectCanvas.setPaint(theZ % 2 == 0 ? trackColorDark.get() : trackColorLight.get());

                // Left and right part of the Road
                aEffectCanvas.fillPolygon(new float[] {0, theOldDataX[0], tempNewXData[0], 0}, new float[] {theOldDataY[1], theOldDataY[1], tempNewYData[1], tempNewYData[1]}, 4);
                aEffectCanvas.fillPolygon(new float[] {theOldDataX[3], screenSize.width, screenSize.width, tempNewXData[3]}, new float[] {theOldDataY[3], theOldDataY[3], tempNewYData[3], tempNewYData[3]}, 4);

                // The curbs
                aEffectCanvas.setPaint(theZ % 2 == 0 ? curbColorLight.get() : curbColorDark.get());
                aEffectCanvas.fillPolygon(new float[] {theOldDataX[0], theOldDataX[1], tempNewXData[1], tempNewXData[0]}, new float[] {theOldDataY[0], theOldDataY[1], tempNewYData[1], tempNewYData[0]}, 4);
                aEffectCanvas.fillPolygon(new float[] {theOldDataX[3], theOldDataX[2], tempNewXData[2], tempNewXData[3]}, new float[] {theOldDataY[3], theOldDataY[2], tempNewYData[2], tempNewYData[3]}, 4);

                // The road itself
                // Calculate the color according the the real track data
                aEffectCanvas.setPaint(theZ % 2 == 0 ? roadColorDark.get() : roadColorLight.get());
                aEffectCanvas.fillPolygon(new float[] {theOldDataX[1], theOldDataX[2], tempNewXData[2], tempNewXData[1]}, new float[] {theOldDataY[1], theOldDataY[2], tempNewYData[2], tempNewYData[1]}, 4);

                // Keep track of the old positions to prevent them fro being recomputed
                theOldDataX = tempNewXData;
                theOldDataY = tempNewYData;
            }

            theXoffset -= theRoad.getCurveFactor();
        }

        // Draw some debug data
        for (int theZ = theFarestPosition; theZ>=theNearestPosition; theZ--) {
            TrackElement theElement1 = track.getTrackElementForPosition((int) theZ - 1);
            TrackElement theElement2 = track.getTrackElementForPosition((int) theZ);

            float theX = 85 + (theZ - thePositionOnTrack) * 15;
            float theY = 200 + theElement1.getHeight() * 30;
            float theX2 = 85 + (theZ - thePositionOnTrack + 1) * 15;
            float theY2 = 200 + theElement2.getHeight() * 30;

            aEffectCanvas.setPaint(new Color(0, 255, 0));
            aEffectCanvas.drawLine(theX, theY, theX2, theY2);

            aEffectCanvas.drawLine(theX, theY - 5, theX, theY + 5);

            aEffectCanvas.setPaint(new Color(255,0,0));
            float thePlayerPos = (thePositionOnTrack % 1) * 15;
            aEffectCanvas.drawLine(100 + thePlayerPos, 100, 100 + thePlayerPos , 300);
        }
        aEffectCanvas.setPaint(new Color(0,0,255));
        aEffectCanvas.drawLine(100, 200 - (theHeightOffset * 30), 800, 200 - (theHeightOffset * 30));
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
        return GameProcess.ProceedResult.CONTINUE_RUNNING;
    }
}