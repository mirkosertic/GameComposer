package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.type.ResourceName;

import java.io.IOException;
import org.junit.Test;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GameResourceCacheTest {

    @Test
    public void testGetResourceFor() throws Exception {
        ResourceName theName = new ResourceName("Test");
        GameResource theResource = mock(GameResource.class);

        GameResourceLoader theLoader = mock(GameResourceLoader.class);
        when(theLoader.load(eq(theName))).thenReturn(theResource);
        GameResourceCache theCache = new GameResourceCache(theLoader);

        assertSame(theResource, theCache.getResourceFor(theName));
        assertSame(theResource, theCache.getResourceFor(theName));
        verify(theLoader,times(1)).load(eq(theName));
    }

    @Test(expected = IOException.class)
    public void testGetResourceForError() throws Exception {
        ResourceName theName = new ResourceName("Test");

        GameResourceLoader theLoader = mock(GameResourceLoader.class);
        GameResourceCache theCache = new GameResourceCache(theLoader);
        theCache.getResourceFor(theName);
    }

    @Test
    public void testFlush() throws Exception {
        ResourceName theName = new ResourceName("Test");
        GameResource theResource = mock(GameResource.class);

        GameResourceLoader theLoader = mock(GameResourceLoader.class);
        when(theLoader.load(eq(theName))).thenReturn(theResource);
        GameResourceCache theCache = new GameResourceCache(theLoader);

        assertSame(theResource, theCache.getResourceFor(theName));
        theCache.flush();
        verify(theLoader).flush();
        assertSame(theResource, theCache.getResourceFor(theName));
        verify(theLoader,times(2)).load(eq(theName));
    }
}