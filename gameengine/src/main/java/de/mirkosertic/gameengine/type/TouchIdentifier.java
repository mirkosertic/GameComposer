package de.mirkosertic.gameengine.type;

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

        return identifier == that.identifier;

    }

    @Override
    public int hashCode() {
        return identifier;
    }
}
