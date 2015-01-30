package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventClassInformation;

@InheritedClassInformation
public class GameSceneEffectAddedToScene extends GameEvent {

    private static final GameSceneEffectAddedToSceneClassInformation CIINSTANCE = new GameSceneEffectAddedToSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameSceneEffect instance;

    public GameSceneEffectAddedToScene(GameScene aScene, GameSceneEffect aInstance) {
        super("SceneEffectAddedToScene");
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameSceneEffectAddedToSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}