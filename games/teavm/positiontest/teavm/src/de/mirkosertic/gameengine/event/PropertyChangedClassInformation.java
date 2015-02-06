package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.*;

public class PropertyChangedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Method<de.mirkosertic.gameengine.event.PropertyChanged> GETOWNER = new Method<de.mirkosertic.gameengine.event.PropertyChanged>("getOwner", Object.class, new Class[] {}) {
    @Override
    public Object invoke(PropertyChanged aObject, Object[] aArguments) {
      return aObject.getOwner();
    }
  };

  public static final Field<PropertyChanged, de.mirkosertic.gameengine.event.Property> PROPERTY = new Field<PropertyChanged, de.mirkosertic.gameengine.event.Property>("property", de.mirkosertic.gameengine.event.Property.class) {
    @Override
    public de.mirkosertic.gameengine.event.Property getValue(PropertyChanged aObject) {
      return aObject.property;
    }
  };

  public static final Field<PropertyChanged, Object> OLDVALUE = new Field<PropertyChanged, Object>("oldValue", Object.class) {
    @Override
    public Object getValue(PropertyChanged aObject) {
      return aObject.oldValue;
    }
  };

  public PropertyChangedClassInformation() {
    register(GETOWNER);
    register(PROPERTY);
    register(OLDVALUE);
  }
}
