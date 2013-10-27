package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class GameObjectInstance {

    private GameObject gameObject;
    private Map<Class<GameComponent>, GameComponent> components;
    private Position position;
    private Size size;

    GameObjectInstance(GameObject aObject) {
        gameObject = aObject;
        components = new HashMap<Class<GameComponent>, GameComponent>();
        position = new Position();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    void addComponent(GameComponent aComponent) {
        components.put((Class<GameComponent>) aComponent.getClass(), aComponent);
    }

    public <T extends GameComponent> T getComponent(Class<T> aComponentClass) {
        return (T) components.get(aComponentClass);
    }
}