package de.mirkosertic.gameengine.starfield;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectUnmarshaller;

import java.util.Map;

public class StarfieldGameSceneEffectUnmarshaller implements GameSceneEffectUnmarshaller {

    @Override
    public String getTypeKey() {
        return StarfieldGameSceneEffect.TYPE_VALUE;
    }

    @Override
    public GameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData) {
        return StarfieldGameSceneEffect.unmarshall(aGameRuntime, aScene, aObjectData);
    }
}
