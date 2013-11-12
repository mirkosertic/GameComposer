package de.mirkosertic.gameengine.action;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ActionUnmarshaller;
import de.mirkosertic.gameengine.core.IORegistry;

import java.util.Map;

public class PlaySoundActionUnmarshaller implements ActionUnmarshaller {

    @Override
    public String getTypeKey() {
        return PlaySoundAction.TYPE_VALUE;
    }

    @Override
    public Action unmarshall(IORegistry aRegistry, Map<String, Object> aSerializedData) {
        return PlaySoundAction.unmarshall(aSerializedData);
    }
}
