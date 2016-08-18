package de.mirkosertic.gameengine.type;

public abstract class CollisionPosition {

    public static final CollisionPosition EVERYWHERE = new CollisionPosition("EVERYWHERE") {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return true;
        }
    };

    public static final CollisionPosition TOP = new CollisionPosition("TOP") {
        // Test if instance2 is above instance 1
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.y < aInstance1.y;
        }
    };

    public static final CollisionPosition BOTTOM = new CollisionPosition("BOTTOM") {
        // Test if instance2 is below instance 1
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.y > aInstance1.y;
        }
    };

    public static final CollisionPosition LEFT = new CollisionPosition("LEFT") {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.x < aInstance1.x;
        }
    };

    public static final CollisionPosition RIGHT = new CollisionPosition("RIGHT") {
        @Override
        public boolean detect(Position aInstance1, Position aInstance2) {
            return aInstance2.x > aInstance1.x;
        }
    };

    private final String name;

    public CollisionPosition(String aName) {
        name = aName;
    }

    public String name() {
        return name;
    }

    public abstract boolean detect(Position aInstance1, Position aInstance2);

    public static CollisionPosition valueOf(String aName) {
        switch (aName) {
            case "EVERYWHERE":
                return EVERYWHERE;
            case "TOP":
                return TOP;
            case "BOTTOM":
                return BOTTOM;
            case "LEFT":
                return LEFT;
            case "RIGHT":
                return RIGHT;
        }
        throw new IllegalArgumentException(aName);
    }

    public static CollisionPosition[] values() {
        return new CollisionPosition[] {EVERYWHERE, TOP, LEFT, BOTTOM, RIGHT};
    }
}