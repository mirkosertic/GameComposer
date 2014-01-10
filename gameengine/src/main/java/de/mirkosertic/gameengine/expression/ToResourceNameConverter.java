package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.ResourceName;

class ToResourceNameConverter implements AutomaticResultConverter<Object, ResourceName> {

    @Override
    public ResourceName convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof ResourceName) {
            return (ResourceName) aValue;
        }
        if (aValue instanceof String) {
            return new ResourceName((String) aValue);
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
