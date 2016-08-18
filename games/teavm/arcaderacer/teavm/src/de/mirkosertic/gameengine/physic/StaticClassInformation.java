package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class StaticClassInformation extends ClassInformation {

  public final Method<de.mirkosertic.gameengine.physic.Static> DELETE = new Method<de.mirkosertic.gameengine.physic.Static>("delete", new Class[] {}) {
    @Override
    public Object invoke(Static aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public StaticClassInformation() {
    register(DELETE);
  }
}
