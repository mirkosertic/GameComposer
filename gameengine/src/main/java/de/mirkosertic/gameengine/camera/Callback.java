package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

public interface Callback {

    boolean accepts(GameObjectInstance aInstance);

    void process(GameObjectInstance aInstance, Position aPositionOnScreen, Size aSize);
}