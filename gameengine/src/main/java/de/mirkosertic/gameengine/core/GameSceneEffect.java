package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.camera.CameraBehavior;
import de.mirkosertic.gameengine.type.ClassInformation;
import de.mirkosertic.gameengine.type.EffectCanvas;

import java.util.Map;

public interface GameSceneEffect {

    String TYPE_ATTRIBUTE = "type";

    GameSceneEffectType getEffectType();

    ClassInformation getClassInformation();

    Map<String,Object> serialize();

    void render(EffectCanvas aEffectCanvas, CameraBehavior aCameraBehavior);

    GameScene getScene();
}