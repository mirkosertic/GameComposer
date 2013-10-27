package de.mirkosertic.gameengine.core;

import java.util.*;

public class GameObject {

    private String name;
    private Map<Class<GameComponentFactory>, GameComponentFactory> components;

    public GameObject(String aName) {
        name = aName;
        components = new HashMap<Class<GameComponentFactory>, GameComponentFactory>();
    }

    public String getName() {
        return name;
    }

    public void add(GameComponentFactory aComponentFactory) {
        components.put((Class<GameComponentFactory>) aComponentFactory.getClass(), aComponentFactory);
    }

    public Set<GameComponentFactory> getComponents() {
        HashSet<GameComponentFactory> theResult = new HashSet<GameComponentFactory>();
        theResult.addAll(components.values());
        return theResult;
    }
}
