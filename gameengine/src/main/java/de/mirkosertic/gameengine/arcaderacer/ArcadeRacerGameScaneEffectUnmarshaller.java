package de.mirkosertic.gameengine.arcaderacer;

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.GameSceneEffect;
import de.mirkosertic.gameengine.core.GameSceneEffectUnmarshaller;

import java.util.Map;

public class ArcadeRacerGameScaneEffectUnmarshaller implements GameSceneEffectUnmarshaller {

    @Override
    public String getTypeKey() {
        return ArcadeRacerGameSceneEffect.TYPE_VALUE;
    }

    @Override
    public GameSceneEffect unmarshall(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> aObjectData) {
        return ArcadeRacerGameSceneEffect.unmarshall(aGameRuntime, aScene, aObjectData);
    }
}
