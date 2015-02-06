package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class TypeConvertersClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.TypeConverters> TEXTEXPRESSION = new Method<de.mirkosertic.gameengine.type.TypeConverters>("textexpression", de.mirkosertic.gameengine.type.TextExpression.class, new Class[] {String.class}) {
    @Override
    public Object invoke(TypeConverters aObject, Object[] aArguments) {
      return aObject.textexpression((String) aArguments[0]);
    }
  };

  public TypeConvertersClassInformation() {
    register(TEXTEXPRESSION);
  }
}
