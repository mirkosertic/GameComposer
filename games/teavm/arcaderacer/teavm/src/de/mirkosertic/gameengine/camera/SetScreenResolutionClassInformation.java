package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class SetScreenResolutionClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<SetScreenResolution, de.mirkosertic.gameengine.type.Size> SCREENSIZE = new Field<SetScreenResolution, de.mirkosertic.gameengine.type.Size>("screenSize", de.mirkosertic.gameengine.type.Size.class) {
    @Override
    public de.mirkosertic.gameengine.type.Size getValue(SetScreenResolution aObject) {
      return aObject.screenSize;
    }
  };

  public SetScreenResolutionClassInformation() {
    register(SCREENSIZE);
  }
}
