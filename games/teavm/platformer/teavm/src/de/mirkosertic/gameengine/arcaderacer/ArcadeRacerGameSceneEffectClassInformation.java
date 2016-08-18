package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class ArcadeRacerGameSceneEffectClassInformation extends ClassInformation {

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>> POSITIONONTRACK = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>>("positionOnTrack", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Double> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.positionOnTrack();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>> CAMERAHEIGHT = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>>("cameraHeight", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Double> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.cameraHeight();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>> SPEED = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>>("speed", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Double> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.speed();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>> FIELDOFVIEW = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>>("fieldOfView", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Integer> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.fieldOfView();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>> VIEWDEPTH = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>>("viewDepth", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Integer> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.viewDepth();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>> DISTANCECAMERA = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>>("distanceCamera", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Integer> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.distanceCamera();
    }
  };

  public final Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>> GETSPEED = new Field<ArcadeRacerGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Double>>("getSpeed", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Double> getValue(ArcadeRacerGameSceneEffect aObject) {
      return aObject.getSpeed();
    }
  };

  public ArcadeRacerGameSceneEffectClassInformation() {
    register(POSITIONONTRACK);
    register(CAMERAHEIGHT);
    register(SPEED);
    register(FIELDOFVIEW);
    register(VIEWDEPTH);
    register(DISTANCECAMERA);
    register(GETSPEED);
  }
}
