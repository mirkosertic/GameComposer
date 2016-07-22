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
    public static final String ZPROJECTIONFACTOR_PROPERTY = "zProjectionFactor";

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
    private final Property<Float> zProjectionFactor;
    private final Property<Float> speed;

    public ArcadeRacerGameSceneEffect(GameScene aScene,
            GameEventManager aEventManager) {
        gameScene = aScene;

        trackColorDark = new Property<>(Color.class, this, TRACK_COLOR_DARK_PROPERTY, new Color(0, 200, 0), aEventManager);
        trackColorLight = new Property<>(Color.class, this, TRACK_COLOR_LIGHT_PROPERTY, new Color(0, 220, 0), aEventManager);
        roadColorDark = new Property<>(Color.class, this, ROAD_COLOR_DARK_PROPERTY, new Color(160, 160, 160), aEventManager);
        roadColorLight = new Property<>(Color.class, this, ROAD_COLOR_LIGHT_PROPERTY, new Color(180, 180, 180), aEventManager);
        curbColorDark = new Property<>(Color.class, this, CURB_COLOR_DARK_PROPERTY, new Color(255, 0, 0), aEventManager);
        curbColorLight = new Property<>(Color.class, this, CURB_COLOR_LIGHT_PROPERTY, new Color(255, 255, 255), aEventManager);
        speed = new Property<>(Float.class, this, SPEED_PROPERTY, 3f, aEventManager);

        positionOnTrack = new Property<>(Float.class, this, POSITIONONTRACK_PROPERTY, 0f, aEventManager);
        distanceProjectionPlane = new Property<>(Float.class, this, DISTANCEPROJECTIONPLANE_PROPERTY, 3f, aEventManager);
        distanceCar = new Property<>(Float.class, this, DISTANCECAR_PROPERTY, 6f, aEventManager);
        cameraHeight = new Property<>(Float.class, this, CAMERAHEIGHT_PROPERTY, 2f, aEventManager);
        zProjectionFactor = new Property<>(Float.class, this, ZPROJECTIONFACTOR_PROPERTY, 2.5f, aEventManager);

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
    public Property<Float> zProjectionFactor() {
        return zProjectionFactor;
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

        Camera theCamera = new Camera(screenSize, cameraHeight.get(), distanceProjectionPlane.get(),
                zProjectionFactor.get(), positionOnTrack.get());

        int theViewDepth = 60;

        // Ok, first of all we need to compute the track to draw
        int theFaresPosition = (int) Math.ceil(positionOnTrack.get() + distanceCar.get() + theViewDepth);
        int theNearestPosition = theFaresPosition - theViewDepth - 4;

        // Now we draw the track
        for (double theZ = theFaresPosition; theZ >= theNearestPosition; theZ--) {

            // Calculate the color according the the real track data
            aEffectCanvas.setPaint(theZ % 2 == 0 ? trackColorDark.get() : trackColorLight.get());

            Point2D theRoadLeftFarOutside  = theCamera.project(new Point3D(-2, 0, theZ));
            Point2D theRoadRightFarOutside  = theCamera.project(new Point3D(2, 0, theZ));

            Point2D theRoadLeftFarInside  = theCamera.project(new Point3D(-1.8, 0, theZ));
            Point2D theRoadRightFarInside  = theCamera.project(new Point3D(1.8, 0, theZ));

            Point2D theRoadLeftNearOutside  = theCamera.project(new Point3D(-2, 0, theZ - 1));
            Point2D theRoadRightNearOutside  = theCamera.project(new Point3D(2, 0, theZ - 1));

            Point2D theRoadLeftNearInside  = theCamera.project(new Point3D(-1.8, 0, theZ - 1));
            Point2D theRoadRightNearInside  = theCamera.project(new Point3D(1.8, 0, theZ - 1));


            // Left and right part of the Road
            aEffectCanvas.fillPolygon(new int[] {0, theRoadLeftFarOutside.x, theRoadLeftNearOutside.x, 0}, new int[] {theRoadLeftFarOutside.y, theRoadLeftFarOutside.y, theRoadLeftNearOutside.y, theRoadLeftNearOutside.y}, 4);
            aEffectCanvas.fillPolygon(new int[] {theRoadRightFarOutside.x, screenSize.width, screenSize.width, theRoadRightNearOutside.x}, new int[] {theRoadRightFarOutside.y, theRoadRightFarOutside.y, theRoadRightNearOutside.y, theRoadRightNearOutside.y}, 4);

            // The curbs
            aEffectCanvas.setPaint(theZ % 2 == 0 ? curbColorLight.get() : curbColorDark.get());
            aEffectCanvas.fillPolygon(new int[] {theRoadLeftFarOutside.x, theRoadLeftFarInside.x, theRoadLeftNearInside.x, theRoadLeftNearOutside.x}, new int[] {theRoadLeftFarOutside.y, theRoadLeftFarInside.y, theRoadLeftNearInside.y, theRoadLeftNearOutside.y}, 4);
            aEffectCanvas.fillPolygon(new int[] {theRoadRightFarOutside.x, theRoadRightFarInside.x, theRoadRightNearInside.x, theRoadRightNearOutside.x}, new int[] {theRoadRightFarOutside.y, theRoadRightFarInside.y, theRoadRightNearInside.y, theRoadRightNearOutside.y}, 4);

            // The road itself
            // Calculate the color according the the real track data
            aEffectCanvas.setPaint(theZ % 2 == 0 ? roadColorDark.get() : roadColorLight.get());
            aEffectCanvas.fillPolygon(new int[] {theRoadLeftFarInside.x, theRoadRightFarInside.x, theRoadRightNearInside.x, theRoadLeftNearInside.x}, new int[] {theRoadLeftFarInside.y, theRoadRightFarInside.y, theRoadRightNearInside.y, theRoadLeftNearInside.y}, 4);
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
        return GameProcess.ProceedResult.CONTINUE_RUNNING;
    }
}