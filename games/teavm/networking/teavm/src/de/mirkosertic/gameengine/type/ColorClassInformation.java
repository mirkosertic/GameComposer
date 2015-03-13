package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class ColorClassInformation extends ClassInformation {

  public static final Field<Color, Integer> R = new Field<Color, Integer>("r", Integer.class) {
    @Override
    public Integer getValue(Color aObject) {
      return aObject.r;
    }
  };

  public static final Field<Color, Integer> G = new Field<Color, Integer>("g", Integer.class) {
    @Override
    public Integer getValue(Color aObject) {
      return aObject.g;
    }
  };

  public static final Field<Color, Integer> B = new Field<Color, Integer>("b", Integer.class) {
    @Override
    public Integer getValue(Color aObject) {
      return aObject.b;
    }
  };

  public ColorClassInformation() {
    register(R);
    register(G);
    register(B);
  }
}
