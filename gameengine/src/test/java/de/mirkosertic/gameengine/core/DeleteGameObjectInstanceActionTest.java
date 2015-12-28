package de.mirkosertic.gameengine.core;

import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class DeleteGameObjectInstanceActionTest {

    @Test
    public void testInvoke() throws Exception {
        GameScene theScene = mock(GameScene.class);
        GameObjectInstance theInstance1 = mock(GameObjectInstance.class);
        GameObjectInstance theInstance2 = mock(GameObjectInstance.class);
        ConditionResult theResult = new ConditionResult(true, null, new GameObjectInstance[] {theInstance1, theInstance2});

        DeleteGameObjectInstanceAction theAction = new DeleteGameObjectInstanceAction();
        theAction.invoke(theScene, theResult);

        verify(theScene).removeGameObjectInstance(theInstance1);
        verify(theScene).removeGameObjectInstance(theInstance2);
    }

    @Test
    public void testSerialize() throws Exception {
        DeleteGameObjectInstanceAction theAction = new DeleteGameObjectInstanceAction();
        Map<String, Object> theData = theAction.serialize();
        assertEquals(1, theData.size());
        assertEquals(DeleteGameObjectInstanceAction.TYPE_VALUE, theData.get(Action.TYPE_ATTRIBUTE));
    }

    @Test
    public void testUnmarshall() throws Exception {
        assertTrue(DeleteGameObjectInstanceAction.unmarshall() instanceof DeleteGameObjectInstanceAction);
    }
}