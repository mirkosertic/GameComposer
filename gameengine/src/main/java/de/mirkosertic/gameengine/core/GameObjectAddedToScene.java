package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class GameObjectAddedToScene extends GameEvent {

    private static final GameObjectAddedToSceneClassInformation CIINSTANCE = new GameObjectAddedToSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameObject object;

    public GameObjectAddedToScene(GameScene aScene, GameObject aObject) {
        super("GameObjectAddedToScene");
        scene = aScene;
        object = aObject;
    }

    @Override
    public GameObjectAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}