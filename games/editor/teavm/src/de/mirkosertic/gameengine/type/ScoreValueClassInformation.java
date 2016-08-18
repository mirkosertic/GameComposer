package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class ScoreValueClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.type.ScoreValue> INCREMENTBY = new Method<de.mirkosertic.gameengine.type.ScoreValue>("incrementBy", new Class[] {Long.class}) {
    @Override
    public Object invoke(ScoreValue aObject, Object[] aArguments) {
      return aObject.incrementBy((Long) aArguments[0]);
    }
  };

  public final Method<de.mirkosertic.gameengine.type.ScoreValue> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.ScoreValue>("resetToZero", new Class[] {}) {
    @Override
    public Object invoke(ScoreValue aObject, Object[] aArguments) {
      return aObject.resetToZero();
    }
  };

  public ScoreValueClassInformation() {
    register(INCREMENTBY);
    register(RESETTOZERO);
  }
}
