package de.mirkosertic.gameengine.starfield;

import de.mirkosertic.gameengine.type.*;

public class StarfieldGameSceneEffectClassInformation extends ClassInformation {

  public static final Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>> NUMBEROFSTARS = new Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Integer>>("numberofStars", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Integer> getValue(StarfieldGameSceneEffect aObject) {
      return aObject.numberofStars();
    }
  };

  public static final Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Float>> STARSPEED = new Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<java.lang.Float>>("starSpeed", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Float> getValue(StarfieldGameSceneEffect aObject) {
      return aObject.starSpeed();
    }
  };

  public static final Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>> COLOR = new Field<StarfieldGameSceneEffect, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color>>("color", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Color> getValue(StarfieldGameSceneEffect aObject) {
      return aObject.color();
    }
  };

  public StarfieldGameSceneEffectClassInformation() {
    register(NUMBEROFSTARS);
    register(STARSPEED);
    register(COLOR);
  }
}
