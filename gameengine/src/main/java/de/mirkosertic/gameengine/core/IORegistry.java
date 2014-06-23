package de.mirkosertic.gameengine.core;

import java.util.HashMap;
import java.util.Map;

public class IORegistry {

    private final Map<String, BehaviorTemplateUnmarshaller> registeredBehaviorTemplateUnmarshaller;
    private final Map<String, BehaviorUnmarshaller> registeredBehaviorUnmarshaller;
    private final Map<String, ConditionUnmarshaller> registeredConditionUnmarshaller;
    private final Map<String, ActionUnmarshaller> registeredActionUnmarshaller;

    IORegistry() {
        registeredBehaviorTemplateUnmarshaller = new HashMap<>();
        registeredBehaviorUnmarshaller = new HashMap<>();
        registeredConditionUnmarshaller = new HashMap<>();
        registeredActionUnmarshaller = new HashMap<>();
    }

    public void registerBehaviorTemplateUnmarshaller(BehaviorTemplateUnmarshaller aUnmarshaller) {
        registeredBehaviorTemplateUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerBehaviorUnmarshaller(BehaviorUnmarshaller aUnmarshaller) {
        registeredBehaviorUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerConditionUnmarshaller(ConditionUnmarshaller aUnmarshaller) {
        registeredConditionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public void registerActionUnmarshaller(ActionUnmarshaller aUnmarshaller) {
        registeredActionUnmarshaller.put(aUnmarshaller.getTypeKey(), aUnmarshaller);
    }

    public BehaviorTemplateUnmarshaller getBehaviorTemplateUnmarshallerFor(String aTypeKey) {
        return registeredBehaviorTemplateUnmarshaller.get(aTypeKey);
    }

    public BehaviorUnmarshaller getBehaviorUnmarshallerFor(String aTypeKey) {
        return registeredBehaviorUnmarshaller.get(aTypeKey);
    }

    public ConditionUnmarshaller getConditionUnmarshallerFor(String aTypeKey) {
        return registeredConditionUnmarshaller.get(aTypeKey);
    }

    public ActionUnmarshaller getActionUnmarshallerFor(String aTypeKey) {
        return registeredActionUnmarshaller.get(aTypeKey);
    }
}