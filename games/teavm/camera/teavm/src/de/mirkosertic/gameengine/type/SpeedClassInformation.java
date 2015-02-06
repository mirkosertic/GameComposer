package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class SpeedClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.Speed> INCREMENTBY = new Method<de.mirkosertic.gameengine.type.Speed>("incrementBy", de.mirkosertic.gameengine.type.Speed.class, new Class[] {Integer.class}) {
    @Override
    public Object invoke(Speed aObject, Object[] aArguments) {
      return aObject.incrementBy((Integer) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Speed> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.Speed>("resetToZero", de.mirkosertic.gameengine.type.Speed.class, new Class[] {}) {
    @Override
    public Object invoke(Speed aObject, Object[] aArguments) {
      return aObject.resetToZero();
    }
  };

  public SpeedClassInformation() {
    register(INCREMENTBY);
    register(RESETTOZERO);
  }
}
