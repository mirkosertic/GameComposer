package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.type.*;

public class PositionClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.type.Position> ADD = new Method<de.mirkosertic.gameengine.type.Position>("add", de.mirkosertic.gameengine.type.Position.class, new Class[] {de.mirkosertic.gameengine.type.Position.class}) {
    @Override
    public Object invoke(Position aObject, Object[] aArguments) {
      return aObject.add((de.mirkosertic.gameengine.type.Position) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Position> CHANGEX = new Method<de.mirkosertic.gameengine.type.Position>("changeX", de.mirkosertic.gameengine.type.Position.class, new Class[] {Float.class}) {
    @Override
    public Object invoke(Position aObject, Object[] aArguments) {
      return aObject.changeX((Float) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Position> CHANGEY = new Method<de.mirkosertic.gameengine.type.Position>("changeY", de.mirkosertic.gameengine.type.Position.class, new Class[] {Float.class}) {
    @Override
    public Object invoke(Position aObject, Object[] aArguments) {
      return aObject.changeY((Float) aArguments[0]);
    }
  };

  public static final Method<de.mirkosertic.gameengine.type.Position> SNAPTOGRID = new Method<de.mirkosertic.gameengine.type.Position>("snapToGrid", de.mirkosertic.gameengine.type.Position.class, new Class[] {Integer.class, Integer.class}) {
    @Override
    public Object invoke(Position aObject, Object[] aArguments) {
      return aObject.snapToGrid((Integer) aArguments[0], (Integer) aArguments[1]);
    }
  };

  public static final Field<Position, Float> X = new Field<Position, Float>("x", Float.class) {
    @Override
    public Float getValue(Position aObject) {
      return aObject.x;
    }
  };

  public static final Field<Position, Float> Y = new Field<Position, Float>("y", Float.class) {
    @Override
    public Float getValue(Position aObject) {
      return aObject.y;
    }
  };

  public PositionClassInformation() {
    register(ADD);
    register(CHANGEX);
    register(CHANGEY);
    register(SNAPTOGRID);
    register(X);
    register(Y);
  }
}
