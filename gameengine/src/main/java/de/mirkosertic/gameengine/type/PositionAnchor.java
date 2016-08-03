package de.mirkosertic.gameengine.type;

public enum PositionAnchor {
    SCENE {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            return aPosition;
        }
    },
    CENTER {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            int aCenterX = aScreenSize.width / 2;
            int aCenterY = aScreenSize.height / 2;
            return new Position(aCenterX + aPosition.x, aCenterY + aPosition.y);
        }
    },
    PERCENT {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            int aX = (int)(aScreenSize.width / 100d * aPosition.x);
            int aY = (int)(aScreenSize.height / 100d * aPosition.y);
            return new Position(aX, aY);
        }
    },
    TOP_LEFT {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            return aPosition;
        }
    },
    TOP_RIGHT {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aPosition.x, aPosition.y);
        }
    },
    BOTTOM_LEFT {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            return new Position(aPosition.x, aScreenSize.height - aPosition.y);
        }
    },
    BOTTOM_RIGHT {
        @Override
        public Position compute(Position aPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aPosition.x, aScreenSize.height - aPosition.y);
        }
    };

    private PositionAnchor() {
    }

    public abstract Position compute(Position aPosition, Size aScreenSize);
}