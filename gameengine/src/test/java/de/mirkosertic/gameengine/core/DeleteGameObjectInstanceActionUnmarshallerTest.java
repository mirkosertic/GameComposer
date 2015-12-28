package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class DeleteGameObjectInstanceActionUnmarshallerTest {

    @Test
    public void testGetTypeKey() throws Exception {
        DeleteGameObjectInstanceActionUnmarshaller theUnmarshaller = new DeleteGameObjectInstanceActionUnmarshaller();
        assertEquals(DeleteGameObjectInstanceAction.TYPE_VALUE, theUnmarshaller.getTypeKey());
    }

    @Test
    public void testUnmarshall() throws Exception {
        DeleteGameObjectInstanceActionUnmarshaller theUnmarshaller = new DeleteGameObjectInstanceActionUnmarshaller();
        Map<String, Object> theData = new HashMap<>();
        GameScene theScene = mock(GameScene.class);
        assertTrue(theUnmarshaller.unmarshall(theData, theScene) instanceof DeleteGameObjectInstanceAction);
    }
}