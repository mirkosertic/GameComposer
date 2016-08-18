package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class PlaySoundClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<PlaySound, de.mirkosertic.gameengine.type.ResourceName> RESOURCENAME = new Field<PlaySound, de.mirkosertic.gameengine.type.ResourceName>("resourceName", de.mirkosertic.gameengine.type.ResourceName.class) {
    @Override
    public de.mirkosertic.gameengine.type.ResourceName getValue(PlaySound aObject) {
      return aObject.resourceName;
    }
  };

  public PlaySoundClassInformation() {
    register(RESOURCENAME);
  }
}
