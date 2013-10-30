package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameObject {

    private String name;
    private Map<Class<GameComponentTemplate>, GameComponentTemplate> componentTemplates;

    public GameObject(String aName) {
        name = aName;
        componentTemplates = new HashMap<>();
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void add(GameComponentTemplate aComponentFactory) {
        componentTemplates.put((Class<GameComponentTemplate>) aComponentFactory.getClass(), aComponentFactory);
    }

    public Set<GameComponentTemplate> getComponentTemplates() {
        HashSet<GameComponentTemplate> theResult = new HashSet<GameComponentTemplate>();
        theResult.addAll(componentTemplates.values());
        return theResult;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("name", name);

        List<Map<String, Object>> theTemplates = new ArrayList<>();
        for (GameComponentTemplate theTemplate : componentTemplates.values()) {
           theTemplates.add(theTemplate.serialize());
        }
        theResult.put("templates", theTemplates);
        return theResult;
    }

    public static GameObject deserialize(GameRuntime aGameRuntime, Map<String, Object> theSerializedData) {
        String theName = (String) theSerializedData.get("name");
        GameObject theObject = new GameObject(theName);

        List<Map<String, Object>> theTemplates = (List<Map<String, Object>>) theSerializedData.get("templates");
        for (Map<String, Object> theTemplate : theTemplates) {
            String theTypeKey = (String) theTemplate.get(GameComponent.TYPE_ATTRIBUTE);
            theObject.add(aGameRuntime.getTemplateUnmarshallerFor(theTypeKey).deserialize( theTemplate));
        }

        return theObject;
    }
}
