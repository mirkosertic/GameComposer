package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.type.*;

public class StaticClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.physic.Static> DELETE = new Method<de.mirkosertic.gameengine.physic.Static>("delete", void.class, new Class[] {}) {
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
