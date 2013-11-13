package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class IORegistry {

    private Map<String, GameComponentTemplateUnmarshaller> registeredTemplateUnmarshaller;
    private Map<String, GameComponentUnmarshaller> registeredComponentUnmarshaller;
    private Map<String, ConditionUnmarshaller> registeredConditionUnmarshaller;
    private Map<String, ActionUnmarshaller> registeredActionUnmarshaller;

    IORegistry() {
        registeredTemplateUnmarshaller = new HashMap<String, GameComponentTemplateUnmarshaller>();
        registeredComponentUnmarshaller = new HashMap<String, GameComponentUnmarshaller>();
        registeredConditionUnmarshaller = new HashMap<String, ConditionUnmarshaller>();
        registeredActionUnmarshaller = new HashMap<String, ActionUnmarshaller>();
    }

    public void registerTemplateUnmarshaller(GameComponentTemplateUnmarshaller aUnmarshaller) {
        registeredTemplateUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerComponentUnmarshaller(GameComponentUnmarshaller aUnmarshaller) {
        registeredComponentUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerConditionUnmarshaller(ConditionUnmarshaller aUnmarshaller) {
        registeredConditionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerActionUnmarshaller(ActionUnmarshaller aUnmarshaller) {
        registeredActionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public GameComponentTemplateUnmarshaller getTemplateUnmarshallerFor(String aTypeKey) {
        return registeredTemplateUnmarshaller.get(aTypeKey);
    }

    public GameComponentUnmarshaller getComponentUnmarshallerFor(String aTypeKey) {
        return registeredComponentUnmarshaller.get(aTypeKey);
    }

    public ConditionUnmarshaller getConditionUnmarshallerFor(String aTypeKey) {
        return registeredConditionUnmarshaller.get(aTypeKey);
    }

    public ActionUnmarshaller getActionUnmarshallerFor(String aTypeKey) {
        return registeredActionUnmarshaller.get(aTypeKey);
    }
}