package de.mirkosertic.gameengine.types;

public enum CollisionPosition {
    EVERYWHERE {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    },
    TOP {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.y < aInstance1.y;
        }
    },
    BOTTOM {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.y > aInstance1.y;
        }
    },
    LEFT {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.x < aInstance1.x;
        }
    },
    RIGHT {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.x > aInstance1.x;
        }
    };

    public abstract boolean detect(Position aInstance1, Position aInstance2);
}