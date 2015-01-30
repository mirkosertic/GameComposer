package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;

@InheritedClassInformation
public class GameSceneEffectRemovedFromScene extends GameEvent {

    private static final GameSceneEffectRemovedFromSceneClassInformation CIINSTANCE = new GameSceneEffectRemovedFromSceneClassInformation();

    @ReflectiveField
    public final GameScene scene;

    @ReflectiveField
    public final GameSceneEffect instance;

    public GameSceneEffectRemovedFromScene(GameScene aScene, GameSceneEffect aInstance) {
        super("SceneEffectRemovedFromScene");
        scene = aScene;
        instance = aInstance;
    }

    @Override
    public GameSceneEffectRemovedFromSceneClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}