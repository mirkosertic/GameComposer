/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.core;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.junit.Test;

public class PromiseTest {

    @Test
    public void testFulfilled() {
        Promise.NoReturnHandler<String> theResolved = mock(Promise.NoReturnHandler.class);
        Promise.ErrorHandler<String> theRejected = mock(Promise.ErrorHandler.class);
        new Promise<String, String>(new Promise.Executor<String, String>() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                aResolver.resolve("Resolved");
            }
        }).thenContinue(theResolved).thenContinue(theResolved).catchError(theRejected);

        verify(theResolved, times(2)).process(eq("Resolved"));
        verifyZeroInteractions(theRejected);
    }

    @Test
    public void testRejected() {
        Promise.NoReturnHandler<String> theResolved = mock(Promise.NoReturnHandler.class);
        Promise.ErrorHandler<String> theRejected = mock(Promise.ErrorHandler.class);
        new Promise<String, String>(new Promise.Executor<String, String>() {
            @Override
            public void process(PromiseResolver aResolver, PromiseRejector aRejector) {
                aRejector.reject("Rejected", null);
            }
        }).thenContinue(theResolved).catchError(theRejected);

        verify(theRejected, times(1)).process(eq("Rejected"), (Exception) isNull());
        verifyZeroInteractions(theResolved);
    }
}