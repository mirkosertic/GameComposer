package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class GameObjectInstanceLeftLayoutConditionUnmarshallerTest {

    @Test
    public void testGetTypeKey() throws Exception {
        GameObjectInstanceLeftLayoutConditionUnmarshaller theUnmarshaller = new GameObjectInstanceLeftLayoutConditionUnmarshaller();
        assertEquals(GameObjectInstanceLeftLayoutCondition.TYPE_VALUE, theUnmarshaller.getTypeKey());
    }

    @Test
    public void testUnmarshall() throws Exception {
        GameScene theScene = mock(GameScene.class);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceLeftLayoutCondition.TYPE_ATTRIBUTE, GameObjectInstanceLeftLayoutCondition.TYPE_VALUE);

        GameObjectInstanceLeftLayoutConditionUnmarshaller theUnmarshaller = new GameObjectInstanceLeftLayoutConditionUnmarshaller();

        GameObjectInstanceLeftLayoutCondition theCondition = theUnmarshaller.unmarshall(theScene, theData);
        assertTrue(theCondition.gameObjectProperty().isNull());
    }
}