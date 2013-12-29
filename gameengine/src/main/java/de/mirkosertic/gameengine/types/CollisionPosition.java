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
            return true;
        }
    },
    BOTTOM {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    },
    LEFT {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    },
    RIGHT {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    };

    public abstract boolean detect(Position aInstance1, Position aInstance2);
}
