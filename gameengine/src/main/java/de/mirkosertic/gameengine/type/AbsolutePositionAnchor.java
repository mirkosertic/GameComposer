package de.mirkosertic.gameengine.type;

public enum AbsolutePositionAnchor {
    TOP_LEFT(0),
    TOP_RIGHT(1),
    BOTTOM_LEFT(2),
    BOTTOM_RIGHT(3);

    final int ordinal;

    private AbsolutePositionAnchor(int aOrdinal) {
        ordinal = aOrdinal;
    }

    // Ugly implementation, but teavm does not work well with overwritten
    // abstract enum implementations
    public Position compute(Position aAbsolutePosition, Size aScreenSize) {
        switch (ordinal) {
        case 0:
            return aAbsolutePosition;
        case 1:
            return new Position(aScreenSize.width - aAbsolutePosition.x, aAbsolutePosition.y);
        case 2:
            return new Position(aAbsolutePosition.x, aScreenSize.height - aAbsolutePosition.y);
        case 3:
            return new Position(aScreenSize.width - aAbsolutePosition.x, aScreenSize.height - aAbsolutePosition.y);
        default:
            throw new IllegalArgumentException("Undefined position anchor type");
        }
    }
}