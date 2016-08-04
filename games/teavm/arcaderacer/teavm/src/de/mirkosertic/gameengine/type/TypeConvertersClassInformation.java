package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class TypeConvertersClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.type.TypeConverters> TEXTEXPRESSION = new Method<de.mirkosertic.gameengine.type.TypeConverters>("textexpression", new Class[] {String.class}) {
    @Override
    public Object invoke(TypeConverters aObject, Object[] aArguments) {
      return aObject.textexpression((String) aArguments[0]);
    }
  };

  public TypeConvertersClassInformation() {
    register(TEXTEXPRESSION);
  }
}
