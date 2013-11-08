package de.mirkosertic.gameengine.core;

public class GameObjectInstanceAngleChangedEvent extends GameEvent {

    private GameObjectInstance instance;
    private Angle newAngle;

    public GameObjectInstanceAngleChangedEvent(GameObjectInstance aInstance, Angle aNewAngle) {
        instance = aInstance;
        newAngle = aNewAngle;
    }

    public GameObjectInstance getGameObjectInstance() {
        return instance;
    }

    public Angle getNewAngle() {
        return newAngle;
    }
}
