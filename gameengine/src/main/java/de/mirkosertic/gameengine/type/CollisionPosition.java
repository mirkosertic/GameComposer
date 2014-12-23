package de.mirkosertic.gameengine.type;

public enum CollisionPosition {
    EVERYWHERE {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    },
    TOP {
        // Test if instance2 is above instance 1
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.y < aInstance1.y;
        }
    },
    BOTTOM {
        // Test if instance2 is below instance 1
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