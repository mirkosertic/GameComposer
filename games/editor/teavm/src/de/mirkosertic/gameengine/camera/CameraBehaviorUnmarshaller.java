package de.mirkosertic.gameengine.camera;

import de.mirkosertic.gameengine.core.BehaviorUnmarshaller;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;

import java.util.Map;

public class CameraBehaviorUnmarshaller implements BehaviorUnmarshaller<CameraBehavior> {

    @Override
    public String getTypeKey() {
        return CameraBehavior.TYPE;
    }

    @Override
    public CameraBehavior deserialize(GameRuntime aRuntime, GameObjectInstance aObjectInstance, Map<String, Object> aSerializedData) {
        CameraBehavior theResult = CameraBehavior.deserialize(aObjectInstance);
        theResult.registerEvents(aRuntime);
        return theResult;
    }
}
