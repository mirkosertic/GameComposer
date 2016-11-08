package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.Field;
import de.mirkosertic.gameengine.type.Method;

public class SpriteSheetRemovedFromSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public final Field<SpriteSheetRemovedFromScene, de.mirkosertic.gameengine.core.Spritesheet> SPRITESHEET = new Field<SpriteSheetRemovedFromScene, de.mirkosertic.gameengine.core.Spritesheet>("spriteSheet", de.mirkosertic.gameengine.core.Spritesheet.class) {
    @Override
    public de.mirkosertic.gameengine.core.Spritesheet getValue(SpriteSheetRemovedFromScene aObject) {
      return aObject.spriteSheet;
    }
  };

  public SpriteSheetRemovedFromSceneClassInformation() {
    register(SPRITESHEET);
  }
}
