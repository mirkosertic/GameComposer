package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class ScoreValueClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.ScoreValue> INCREMENTBY = new Method<de.mirkosertic.gameengine.type.ScoreValue>("incrementBy", de.mirkosertic.gameengine.type.ScoreValue.class, new Class[] {Long.class}) {
    @Override
    public Object invoke(ScoreValue aObject, Object[] aArguments) {
      return aObject.incrementBy((Long) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.ScoreValue> RESETTOZERO = new Method<de.mirkosertic.gameengine.type.ScoreValue>("resetToZero", de.mirkosertic.gameengine.type.ScoreValue.class, new Class[] {}) {
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
