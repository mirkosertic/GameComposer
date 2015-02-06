package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.*;

public class RunSceneClassInformation extends de.mirkosertic.gameengine.event.GameEventClassInformation {

  public static final Field<RunScene, String> SCENEID = new Field<RunScene, String>("sceneId", String.class) {
    @Override
    public String getValue(RunScene aObject) {
      return aObject.sceneId;
    }
  };

  public RunSceneClassInformation() {
    register(SCENEID);
  }
}
