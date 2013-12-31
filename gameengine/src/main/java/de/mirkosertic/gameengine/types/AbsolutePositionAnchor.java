package de.mirkosertic.gameengine.types;

public enum AbsolutePositionAnchor {
    TOP_LEFT {
        @Override
        public Position compute(Position aAbsolutePosition, Size aScreenSize) {
            return aAbsolutePosition;
        }
    },
    TOP_RIGHT {
        @Override
        public Position compute(Position aAbsolutePosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aAbsolutePosition.x, aAbsolutePosition.y);
        }
    },
    BOTTOM_LEFT {
        @Override
        public Position compute(Position aAbsolutePosition, Size aScreenSize) {
            return new Position(aAbsolutePosition.x, aScreenSize.height - aAbsolutePosition.y);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Position compute(Position aAbsolutePosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aAbsolutePosition.x, aScreenSize.height - aAbsolutePosition.y);
        }
    };

    public abstract Position compute(Position aAbsolutePosition, Size aScreenSize);
}