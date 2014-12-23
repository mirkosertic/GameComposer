package de.mirkosertic.gameengine.core;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class GameObjectInstanceAddedToSceneConditionUnmarshallerTest {

    @Test
    public void testGetTypeKey() throws Exception {
        GameObjectInstanceAddedToSceneConditionUnmarshaller theUnmarshaller = new GameObjectInstanceAddedToSceneConditionUnmarshaller();
        assertEquals(GameObjectInstanceAddedToSceneCondition.TYPE_VALUE, theUnmarshaller.getTypeKey());
    }

    @Test
    public void testUnmarshall() throws Exception {
        GameObjectInstanceAddedToSceneConditionUnmarshaller theUnmarshaller = new GameObjectInstanceAddedToSceneConditionUnmarshaller();

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceAddedToSceneCondition.TYPE_ATTRIBUTE, GameObjectInstanceAddedToSceneCondition.TYPE_VALUE);

        GameScene theScene = mock(GameScene.class);

        assertNotNull(theUnmarshaller.unmarshall(theScene, theData));
    }
}