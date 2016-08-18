package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class CustomPropertiesClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.type.CustomProperties> SET = new Method<de.mirkosertic.gameengine.type.CustomProperties>("set", new Class[] {String.class, String.class}) {
    @Override
    public Object invoke(CustomProperties aObject, Object[] aArguments) {
      aObject.set((String) aArguments[0], (String) aArguments[1]);
      return null;
    }
  };

  public final Method<de.mirkosertic.gameengine.type.CustomProperties> GET = new Method<de.mirkosertic.gameengine.type.CustomProperties>("get", new Class[] {String.class}) {
    @Override
    public Object invoke(CustomProperties aObject, Object[] aArguments) {
      return aObject.get((String) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.type.CustomProperties> REMOVE = new Method<de.mirkosertic.gameengine.type.CustomProperties>("remove", new Class[] {String.class}) {
    @Override
    public Object invoke(CustomProperties aObject, Object[] aArguments) {
      aObject.remove((String) aArguments[0]);
      return null;
    }
  };

  public final Method<de.mirkosertic.gameengine.type.CustomProperties> HAS = new Method<de.mirkosertic.gameengine.type.CustomProperties>("has", new Class[] {String.class}) {
    @Override
    public Object invoke(CustomProperties aObject, Object[] aArguments) {
      return aObject.has((String) aArguments[0]);
    }
  };

  public CustomPropertiesClassInformation() {
    register(SET);
    register(GET);
    register(REMOVE);
    register(HAS);
  }
}
