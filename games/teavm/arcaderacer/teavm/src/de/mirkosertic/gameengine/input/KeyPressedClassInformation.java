package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class KeyPressedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<KeyPressed, de.mirkosertic.gameengine.type.GameKeyCode> KEYCODE = new Field<KeyPressed, de.mirkosertic.gameengine.type.GameKeyCode>("keyCode", de.mirkosertic.gameengine.type.GameKeyCode.class) {
    @Override
    public de.mirkosertic.gameengine.type.GameKeyCode getValue(KeyPressed aObject) {
      return aObject.keyCode;
    }
  };

  public KeyPressedClassInformation() {
    register(KEYCODE);
  }
}
