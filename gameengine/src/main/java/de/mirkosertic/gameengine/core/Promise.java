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

import java.util.ArrayList;
import java.util.List;

public class Promise<T,V> implements PromiseResolver<T>, PromiseRejector<V> {

    public enum State {
        pending,
        resolved,
        rejected;
    }

    public interface Executor<T,V> {
        void process(PromiseResolver<T> aResolver, PromiseRejector<V> aRejector);
    }

    public interface Handler<X,Y> {
        Y process(X aResult);
    }

    public interface NoReturnHandler<X> {
        void process(X aResult);
    }

    public interface ErrorHandler<X> {
        void process(X aResult);
    }

    private State state;
    private T resolvedValue;
    private V rejectedReason;
    private final List<ErrorHandler<V>> rejectFilters;
    private final List<NoReturnHandler<T>> resolveFilters;

    public Promise() {
        this(new Executor<T, V>() {
            @Override
            public void process(PromiseResolver<T> aResolver, PromiseRejector<V> aRejector) {
            }
        });
    }

    public Promise(Executor aExecutor) {
        state = State.pending;
        rejectFilters = new ArrayList<>();
        resolveFilters = new ArrayList<>();
        aExecutor.process(this, this);
    }

    public Promise.State getState() {
        return state;
    }

    public T getResolvedValue() {
        return resolvedValue;
    }

    public V getRejectedReason() {
        return rejectedReason;
    }

    @Override
    public void resolve(T aValue) {
        state = State.resolved;
        resolvedValue = aValue;
        for (NoReturnHandler<T> theFilter : resolveFilters) {
            theFilter.process(resolvedValue);
        }
    }

    @Override
    public void reject(V aReason) {
        state = State.rejected;
        rejectedReason = aReason;
        for (ErrorHandler<V> theFilter : rejectFilters) {
            theFilter.process(aReason);
        }
    }

    public <X> Promise<X, V> thenContinue(final Handler<T, X> aFilter) {
        final Promise<X, V> theNewPromise = new Promise<>();
        switch (state) {
            case pending:
                resolveFilters.add(new NoReturnHandler<T>() {
                    @Override
                    public void process(T aResult) {
                        theNewPromise.resolve(aFilter.process(aResult));
                    }
                });
                break;
            case resolved:
                theNewPromise.resolve(aFilter.process(resolvedValue));
                break;
            case rejected:
                theNewPromise.reject(rejectedReason);
                break;
        }
        return theNewPromise;
    }

    public Promise<T, V> thenContinue(NoReturnHandler<T> aFilter) {
        switch (state) {
            case pending:
                resolveFilters.add(aFilter);
                break;
            case resolved:
                aFilter.process(resolvedValue);
                break;
            case rejected:
                // Nothing to do
                break;
        }
        return this;
    }

    public void catchError(ErrorHandler<V> aFilter) {
        switch (state) {
            case pending:
                rejectFilters.add(aFilter);
                break;
            case rejected:
                aFilter.process(rejectedReason);
                break;
            case resolved:
                // Nothing to do
                break;
        }
    }

    public static Promise<Promise[], Void> all(final Promise... aPromise) {
        final Promise<Promise[], Void> theUnionPromise = new Promise();

        final Promise[] theData = new Promise[aPromise.length];
        for (int i=0;i<aPromise.length;i++) {
            final int theFinalI = i;
            final Promise thePromise = aPromise[i];
            thePromise.thenContinue(new NoReturnHandler() {
                @Override
                public void process(Object aResult) {
                    theData[theFinalI] = thePromise;
                    boolean theComplete = true;
                    for (int k=0;k<aPromise.length;k++) {
                        if (theData[k] == null) {
                            theComplete = false;
                        }
                    }
                    if (theComplete) {
                        theUnionPromise.resolve(theData);
                    }
                }
            }).catchError(new ErrorHandler() {
                @Override
                public void process(Object aResult) {
                    theUnionPromise.reject(null);
                }
            });
        }

        return theUnionPromise;
    }
}
