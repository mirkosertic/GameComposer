package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class BuiltInFunctionsClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.BuiltInFunctions> SYSTIME = new Method<de.mirkosertic.gameengine.type.BuiltInFunctions>("systime", Number.class, new Class[] {}) {
    @Override
    public Object invoke(BuiltInFunctions aObject, Object[] aArguments) {
      return aObject.systime();
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.BuiltInFunctions> FORMATTIME = new Method<de.mirkosertic.gameengine.type.BuiltInFunctions>("formatTime", String.class, new Class[] {Number.class, String.class}) {
    @Override
    public Object invoke(BuiltInFunctions aObject, Object[] aArguments) {
      return aObject.formatTime((Number) aArguments[0], (String) aArguments[1]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.BuiltInFunctions> MAX = new Method<de.mirkosertic.gameengine.type.BuiltInFunctions>("max", Number.class, new Class[] {Number.class, Number.class}) {
    @Override
    public Object invoke(BuiltInFunctions aObject, Object[] aArguments) {
      return aObject.max((Number) aArguments[0], (Number) aArguments[1]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.BuiltInFunctions> MIN = new Method<de.mirkosertic.gameengine.type.BuiltInFunctions>("min", Number.class, new Class[] {Number.class, Number.class}) {
    @Override
    public Object invoke(BuiltInFunctions aObject, Object[] aArguments) {
      return aObject.min((Number) aArguments[0], (Number) aArguments[1]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.BuiltInFunctions> NVL = new Method<de.mirkosertic.gameengine.type.BuiltInFunctions>("nvl", Object.class, new Class[] {Object.class, Object.class}) {
    @Override
    public Object invoke(BuiltInFunctions aObject, Object[] aArguments) {
      return aObject.nvl((Object) aArguments[0], (Object) aArguments[1]);
    }
  };

  public BuiltInFunctionsClassInformation() {
    register(SYSTIME);
    register(FORMATTIME);
    register(MAX);
    register(MIN);
    register(NVL);
  }
}
