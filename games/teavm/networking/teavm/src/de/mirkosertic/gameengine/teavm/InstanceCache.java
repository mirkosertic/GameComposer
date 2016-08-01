package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.teavm.pixi.Container;
import de.mirkosertic.gameengine.teavm.pixi.DisplayObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InstanceCache {

    @FunctionalInterface
    public static interface Producer<T> {

        T create();
    }

    private final Map<String, DisplayObject> instances;
    private final Container container;
    private final Set<String> touchedInstances;

    public InstanceCache(Container aContainer) {
        instances = new HashMap<>();
        container = aContainer;
        touchedInstances = new HashSet<>();
    }

    public <T extends DisplayObject> T getOrCreate(String aObjectID, Producer<T> aProducer, int aZIndex) {
        touchedInstances.add(aObjectID);
        DisplayObject theResult = instances.get(aObjectID);
        if (theResult == null) {
            theResult = aProducer.create();
            theResult.setZOrder(aZIndex);
            instances.put(aObjectID, theResult);
            container.addChild(theResult);
        }
        return (T) theResult;
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
