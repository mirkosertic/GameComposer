package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class ForceClassInformation extends ClassInformation {

  public final Field<Force, Float> FORCEX = new Field<Force, Float>("forceX", Float.class) {
    @Override
    public Float getValue(Force aObject) {
      return aObject.forceX;
    }
  };

  public final Field<Force, Float> FORCEY = new Field<Force, Float>("forceY", Float.class) {
    @Override
    public Float getValue(Force aObject) {
      return aObject.forceY;
    }
  };

  public ForceClassInformation() {
    register(FORCEX);
    register(FORCEY);
  }
}
