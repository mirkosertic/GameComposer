package de.mirkosertic.gameengine.type;

public abstract class Field<V,T> {

    private final String name;
    private final Class type;

    public Field(String aName, Class<?> aType) {
        name = aName;
        type = aType;
    }

    public String getName() {
        return name;
    }

    public Class getType() {
        return type;
    }

    public abstract T getValue(V aObject);
}
