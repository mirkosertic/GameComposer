package de.mirkosertic.gameengine.core;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

public class NaivePromiseTest {

    @Test
    public void testFulfilled() {
        Promise.Filter<String, String, String> theResolved = mock(Promise.Filter.class);
        Promise.Filter<String, String, String> theRejected = mock(Promise.Filter.class);
        new NaivePromise(new Promise.Handler() {
            @Override
            public void process(Promise aPromise) {
                aPromise.resolve("Resolved");
            }
        }).then(theResolved).then(theResolved).catchError(theRejected);

        verify(theResolved, times(2)).process(eq("Resolved"), any(Promise.class));
        verifyZeroInteractions(theRejected);
    }

    @Test
    public void testRejected() {
        Promise.Filter<String, String, String> theResolved = mock(Promise.Filter.class);
        Promise.Filter<String, String, String> theRejected = mock(Promise.Filter.class);
        new NaivePromise(new Promise.Handler() {
            @Override
            public void process(Promise aPromise) {
                aPromise.reject("Rejected");
            }
        }).then(theResolved).catchError(theRejected);

        verify(theRejected, times(1)).process(eq("Rejected"), any(Promise.class));
        verifyZeroInteractions(theResolved);
    }
}