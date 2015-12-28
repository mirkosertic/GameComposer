package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameObjectInstanceRemovedFromSceneConditionUnmarshallerTest {

    @Test
    public void testGetTypeKey() throws Exception {
        GameObjectInstanceRemovedFromSceneConditionUnmarshaller theUnmarshaller = new GameObjectInstanceRemovedFromSceneConditionUnmarshaller();
        assertEquals(GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE, theUnmarshaller.getTypeKey());
    }

    @Test
    public void testUnmarshall() throws Exception {
        GameObjectInstanceRemovedFromSceneConditionUnmarshaller theUnmarshaller = new GameObjectInstanceRemovedFromSceneConditionUnmarshaller();
        GameScene theScene = mock(GameScene.class);
        GameObject theObject = mock(GameObject.class);
        when(theScene.findObjectByID(eq("UUID"))).thenReturn(theObject);

        Map<String, Object> theData = new HashMap<>();
        theData.put(GameObjectInstanceRemovedFromSceneCondition.TYPE_ATTRIBUTE, GameObjectInstanceRemovedFromSceneCondition.TYPE_VALUE);
        GameObjectInstanceRemovedFromSceneCondition theCondition = theUnmarshaller.unmarshall(theScene, theData);
        assertTrue(theCondition.gameObjectProperty().isNull());

        theData.put("objectUUID", "UUID");
        GameObjectInstanceRemovedFromSceneCondition theCondition2 = theUnmarshaller.unmarshall(theScene, theData);
        assertSame(theObject, theCondition2.gameObjectProperty().get());

        verify(theScene, times(1)).findObjectByID(eq("UUID"));
    }
}