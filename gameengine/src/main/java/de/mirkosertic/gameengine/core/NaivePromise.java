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

public class NaivePromise<T,V> implements Promise<T, V> {

    private enum State {
        pending,
        fullfilled,
        rejected;
    }

    private State state;
    private T resolvedValue;
    private V rejectedReason;
    private final List<Filter<T, T, V>> resolvedFilters;
    private final List<Filter<V, T, V>> rejectedFilters;

    public NaivePromise(Handler aHandler) {
        state = State.pending;
        resolvedFilters = new ArrayList<>();
        rejectedFilters = new ArrayList<>();
        aHandler.process(this);
    }

    @Override
    public void resolve(T aValue) {
        resolvedValue = aValue;
        state = State.fullfilled;
        for (Filter<T, T, V> theFilter : resolvedFilters) {
            theFilter.process(resolvedValue, this);
        }
        resolvedFilters.clear();
    }

    @Override
    public Promise<T, V> then(Filter<T, T, V> aFilter) {
        if (state == State.pending) {
            resolvedFilters.add(aFilter);
            resolvedFilters.clear();
            return this;
        }
        if (state == State.rejected) {
            return this;
        }
        aFilter.process(resolvedValue, this);
        return this;
    }

    @Override
    public void catchError(Filter<V, T, V> aFilter) {
        if (state == State.pending) {
            rejectedFilters.add(aFilter);
            return;
        }

        if (state == State.rejected) {
            aFilter.process(rejectedReason, this);
        }
    }

    @Override
    public void reject(V aReason) {
        rejectedReason = aReason;
        state = State.rejected;
        for (Filter<V, T, V> theFilter : rejectedFilters) {
            theFilter.process(rejectedReason, this);
        }
        rejectedFilters.clear();
    }
}