package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class AngleClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.Angle> ADD = new Method<de.mirkosertic.gameengine.type.Angle>("add", de.mirkosertic.gameengine.type.Angle.class, new Class[] {Integer.class}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.add((Integer) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Angle> INVERT = new Method<de.mirkosertic.gameengine.type.Angle>("invert", de.mirkosertic.gameengine.type.Angle.class, new Class[] {}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.invert();
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Angle> SNAPTO = new Method<de.mirkosertic.gameengine.type.Angle>("snapTo", de.mirkosertic.gameengine.type.Angle.class, new Class[] {Integer.class}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.snapTo((Integer) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Angle> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.Angle>("resetToZero", de.mirkosertic.gameengine.type.Angle.class, new Class[] {}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.resetToZero();
    }
  };

  public AngleClassInformation() {
    register(ADD);
    register(INVERT);
    register(SNAPTO);
    register(RESETTOZERO);
  }
}
