package de.mirkosertic.gameengine.type;

public abstract class PositionAnchor {

    public static final PositionAnchor SCENE = new PositionAnchor("SCENE") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aWorldPosition.x - aCameraPosition.x, aWorldPosition.y - aCameraPosition.y);
        }
    };

    public static final PositionAnchor CENTER = new PositionAnchor("CENTER") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            int aCenterX = aScreenSize.width / 2;
            int aCenterY = aScreenSize.height / 2;
            return new Position(aCenterX + aWorldPosition.x, aCenterY + aWorldPosition.y);
        }
    };

    public static final PositionAnchor PERCENT = new PositionAnchor("PERCENT") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            int aX = (int)(aScreenSize.width / 100d * aWorldPosition.x);
            int aY = (int)(aScreenSize.height / 100d * aWorldPosition.y);
            return new Position(aX, aY);
        }
    };

    public static final PositionAnchor TOP_LEFT = new PositionAnchor("TOP_LEFT") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return aWorldPosition;
        }
    };

    public static final PositionAnchor TOP_RIGHT = new PositionAnchor("TOP_RIGHT") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aWorldPosition.x, aWorldPosition.y);
        }
    };

    public static final PositionAnchor BOTTOM_LEFT = new PositionAnchor("BOTTOM_LEFT") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aWorldPosition.x, aScreenSize.height - aWorldPosition.y);
        }
    };

    public static final PositionAnchor BOTTOM_RIGHT = new PositionAnchor("BOTTOM_RIGHT") {
        @Override
        public Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize) {
            return new Position(aScreenSize.width - aWorldPosition.x, aScreenSize.height - aWorldPosition.y);
        }
    };

    private final String name;

    private PositionAnchor(String aName) {
        name = aName;
    }

    public String name() {
        return name;
    }

    public abstract Position compute(Position aWorldPosition, Position aCameraPosition, Size aScreenSize);

    public static PositionAnchor valueOf(String aName) {
        switch (aName) {
            case "TOP_LEFT":
                return TOP_LEFT;
            case "TOP_RIGHT":
                return TOP_RIGHT;
            case "BOTTOM_LEFT":
                return BOTTOM_LEFT;
            case "BOTTOM_RIGHT":
                return BOTTOM_RIGHT;
            case "SCENE":
                return SCENE;
            case "CENTER":
                return CENTER;
            case "PERCENT":
                return PERCENT;
        }
        throw new IllegalArgumentException(aName);
    }

    public static PositionAnchor[] values() {
        return new PositionAnchor[] {SCENE, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, PERCENT};
    }
}