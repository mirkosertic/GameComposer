package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class AngleClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.type.Angle> ADD = new Method<de.mirkosertic.gameengine.type.Angle>("add", new Class[] {Integer.class}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.add((Integer) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.type.Angle> INVERT = new Method<de.mirkosertic.gameengine.type.Angle>("invert", new Class[] {}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.invert();
    }
  };

  public final Method<de.mirkosertic.gameengine.type.Angle> SNAPTO = new Method<de.mirkosertic.gameengine.type.Angle>("snapTo", new Class[] {Integer.class}) {
    @Override
    public Object invoke(Angle aObject, Object[] aArguments) {
      return aObject.snapTo((Integer) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.type.Angle> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.Angle>("resetToZero", new Class[] {}) {
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
