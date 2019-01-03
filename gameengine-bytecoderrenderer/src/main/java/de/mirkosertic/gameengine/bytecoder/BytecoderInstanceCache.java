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
package de.mirkosertic.gameengine.bytecoder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.mirkosertic.gameengine.bytecoder.pixi.Container;
import de.mirkosertic.gameengine.bytecoder.pixi.DisplayObject;

public class BytecoderInstanceCache {

    @FunctionalInterface
    public static interface Producer<T> {

        T create();
    }

    private final Map<String, DisplayObject> instances;
    private final Container container;
    private final Set<String> touchedInstances;

    public BytecoderInstanceCache(Container aContainer) {
        instances = new HashMap<>();
        container = aContainer;
        touchedInstances = new HashSet<>();
    }

    public <T extends DisplayObject> T getOrCreate(String aObjectID, Producer<T> aProducer, int aZIndex) {
        touchedInstances.add(aObjectID);
        DisplayObject theResult = instances.get(aObjectID);
        if (theResult == null) {
            theResult = aProducer.create();
            theResult.zOrder(aZIndex);
            instances.put(aObjectID, theResult);
            container.addChild(theResult);
        }
        return (T) theResult;
    }

    public void touch(String aObjectID) {
        touchedInstances.add(aObjectID);
    }

    public void clearTouchedInstances() {
        touchedInstances.clear();
    }

    public void keepOnlyTouched() {
        // Remove no longer visible instances
        Set<String> theRemovedInstances = new HashSet<>();
        for (Map.Entry<String, DisplayObject> theEntry : instances.entrySet()) {
            if (!touchedInstances.contains(theEntry.getKey())) {
                theRemovedInstances.add(theEntry.getKey());
                container.removeChild(theEntry.getValue());
            }
        }

        for (String theEntry : theRemovedInstances) {
            instances.remove(theEntry);
        }
    }
}
