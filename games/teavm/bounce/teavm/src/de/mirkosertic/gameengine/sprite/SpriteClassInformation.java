package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.type.*;

public class SpriteClassInformation extends ClassInformation {

  public static final Method<de.mirkosertic.gameengine.sprite.Sprite> DELETE = new Method<de.mirkosertic.gameengine.sprite.Sprite>("delete", void.class, new Class[] {}) {
    @Override
    public Object invoke(Sprite aObject, Object[] aArguments) {
      aObject.delete();
      return null;
    }
  };

  public static final Field<Sprite, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Animation>> CURRENTANIMATIONPROPERTY = new Field<Sprite, de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Animation>>("currentAnimationProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<de.mirkosertic.gameengine.type.Animation> getValue(Sprite aObject) {
      return aObject.currentAnimationProperty();
    }
  };

  public static final Field<Sprite, de.mirkosertic.gameengine.event.Property<java.lang.Integer>> SPEEDPROPERTY = new Field<Sprite, de.mirkosertic.gameengine.event.Property<java.lang.Integer>>("speedProperty", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property<java.lang.Integer> getValue(Sprite aObject) {
      return aObject.speedProperty();
    }
  };

  public SpriteClassInformation() {
    register(DELETE);
    register(CURRENTANIMATIONPROPERTY);
    register(SPEEDPROPERTY);
  }
}
