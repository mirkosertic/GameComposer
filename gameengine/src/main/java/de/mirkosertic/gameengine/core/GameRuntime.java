package de.mirkosertic.gameengine.core;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GameRuntime {

    private GameEventManager eventManager;
    private Set<GameSystem> systems;
    private Map<String, GameComponentTemplateUnmarshaller> registeredTemplateUnmarshaller;
    private Map<String, GameComponentUnmarshaller> registeredComponentUnmarshaller;

    public GameRuntime(GameEventManager aEventManager) {
        eventManager = aEventManager;
        systems = new HashSet<GameSystem>();
        registeredTemplateUnmarshaller = new HashMap<String, GameComponentTemplateUnmarshaller>();
        registeredComponentUnmarshaller = new HashMap<String, GameComponentUnmarshaller>();
    }

    public GameEventManager getEventManager() {
        return eventManager;
    }

    public void addSystem(GameSystem aSystem) {
        systems.add(aSystem);
    }

    public Set<GameSystem> getSystems() {
        return Collections.unmodifiableSet(systems);
    }

    public void registeredTemplateUnmarshaller(GameComponentTemplateUnmarshaller aUnmarshaller) {
        registeredTemplateUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registeredComponentUnmarshaller(GameComponentUnmarshaller aUnmarshaller) {
        registeredComponentUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public GameComponentTemplateUnmarshaller getTemplateUnmarshallerFor(String aTypeKey) {
        return registeredTemplateUnmarshaller.get(aTypeKey);
    }

    public GameComponentUnmarshaller getComponentUnmarshallerFor(String aTypeKey) {
        return registeredComponentUnmarshaller.get(aTypeKey);
    }
}