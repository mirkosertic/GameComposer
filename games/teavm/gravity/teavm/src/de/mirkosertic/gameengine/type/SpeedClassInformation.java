package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class SpeedClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.type.Speed> INCREMENTBY = new Method<de.mirkosertic.gameengine.type.Speed>("incrementBy", new Class[] {Integer.class}) {
    @Override
    public Object invoke(Speed aObject, Object[] aArguments) {
      return aObject.incrementBy((Integer) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.type.Speed> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.Speed>("resetToZero", new Class[] {}) {
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
