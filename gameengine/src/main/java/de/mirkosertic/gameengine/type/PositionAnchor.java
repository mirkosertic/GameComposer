package de.mirkosertic.gameengine.type;

public enum PositionAnchor {
    SCENE {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aWorldPosition.x - aCameraPosition.x, aWorldPosition.y - aCameraPosition.y);
        }
    },
    CENTER {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            int aCenterX = aScreenSize.width / 2;
            int aCenterY = aScreenSize.height / 2;
            return new Position(aCenterX + aWorldPosition.x, aCenterY + aWorldPosition.y);
        }
    },
    PERCENT {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            int aX = (int)(aScreenSize.width / 100d * aWorldPosition.x);
            int aY = (int)(aScreenSize.height / 100d * aWorldPosition.y);
            return new Position(aX, aY);
        }
    },
    TOP_LEFT {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return aWorldPosition;
        }
    },
    TOP_RIGHT {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aWorldPosition.x, aWorldPosition.y);
        }
    },
    BOTTOM_LEFT {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aWorldPosition.x, aScreenSize.height - aWorldPosition.y);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aWorldPosition.x, aScreenSize.height - aWorldPosition.y);
        }
    };

    private PositionAnchor() {
    }

    public abstract Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize);
}