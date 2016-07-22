package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class SizeClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.Size> CHANGEWIDTH = new Method<de.mirkosertic.gameengine.type.Size>("changeWidth", de.mirkosertic.gameengine.type.Size.class, new Class[] {Integer.class}) {
    @Override
    public Object invoke(Size aObject, Object[] aArguments) {
      return aObject.changeWidth((Integer) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Size> CHANGEHEIGHT = new Method<de.mirkosertic.gameengine.type.Size>("changeHeight", de.mirkosertic.gameengine.type.Size.class, new Class[] {Integer.class}) {
    @Override
    public Object invoke(Size aObject, Object[] aArguments) {
      return aObject.changeHeight((Integer) aArguments[0]);
    }
  };

  public static final Field<Size, Integer> WIDTH = new Field<Size, Integer>("width", Integer.class) {
    @Override
    public Integer getValue(Size aObject) {
      return aObject.width;
    }
  };

  public static final Field<Size, Integer> HEIGHT = new Field<Size, Integer>("height", Integer.class) {
    @Override
    public Integer getValue(Size aObject) {
      return aObject.height;
    }
  };

  public SizeClassInformation() {
    register(CHANGEWIDTH);
    register(CHANGEHEIGHT);
    register(WIDTH);
    register(HEIGHT);
  }
}
