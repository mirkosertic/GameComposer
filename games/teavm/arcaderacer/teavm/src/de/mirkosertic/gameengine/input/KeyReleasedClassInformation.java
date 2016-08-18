package de.mirkosertic.gameengine.input;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class KeyReleasedClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<KeyReleased, de.mirkosertic.gameengine.type.GameKeyCode> KEYCODE = new Field<KeyReleased, de.mirkosertic.gameengine.type.GameKeyCode>("keyCode", de.mirkosertic.gameengine.type.GameKeyCode.class) {
    @Override
    public de.mirkosertic.gameengine.type.GameKeyCode getValue(KeyReleased aObject) {
      return aObject.keyCode;
    }
  };

  public KeyReleasedClassInformation() {
    register(KEYCODE);
  }
}
