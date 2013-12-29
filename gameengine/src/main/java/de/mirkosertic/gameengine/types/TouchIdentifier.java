package de.mirkosertic.gameengine.types;

public class TouchIdentifier {

    public final int identifier;

    public TouchIdentifier(int aId) {
        identifier = aId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TouchIdentifier that = (TouchIdentifier) o;

        if (identifier != that.identifier) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return identifier;
    }
}
