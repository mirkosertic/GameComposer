package de.mirkosertic.gameengine.core;

import java.util.*;

public class GameObject {

    private String uuid;
    private String name;
    private Map<Class<GameComponentTemplate>, GameComponentTemplate> componentTemplates;

    public GameObject(String aName) {
        this(aName, UUID.randomUID());
    }

    GameObject(String aName, String aUUID) {
        uuid = aUUID;
        name = aName;
        componentTemplates = new HashMap<Class<GameComponentTemplate>, GameComponentTemplate>();
    }

    public String getUuid() {
        return uuid;
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

    public <T extends GameComponentTemplate> T getComponentTemplate(Class<T> aComponentClass) {
        return (T) componentTemplates.get(aComponentClass);
    }

    public Set<GameComponentTemplate> getComponentTemplates() {
        HashSet<GameComponentTemplate> theResult = new HashSet<GameComponentTemplate>();
        theResult.addAll(componentTemplates.values());
        return theResult;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("name", name);
        theResult.put("uuid", uuid);

        List<Map<String, Object>> theTemplates = new ArrayList<Map<String, Object>>();
        for (GameComponentTemplate theTemplate : componentTemplates.values()) {
           theTemplates.add(theTemplate.serialize());
        }
        theResult.put("templates", theTemplates);
        return theResult;
    }

    public static GameObject deserialize(GameRuntime aGameRuntime, Map<String, Object> theSerializedData) {
        String theName = (String) theSerializedData.get("name");
        String theUUID = (String) theSerializedData.get("uuid");
        GameObject theObject = new GameObject(theName, theUUID);

        List<Map<String, Object>> theTemplates = (List<Map<String, Object>>) theSerializedData.get("templates");
        for (Map<String, Object> theTemplate : theTemplates) {
            String theTypeKey = (String) theTemplate.get(GameComponent.TYPE_ATTRIBUTE);
            theObject.add(aGameRuntime.getTemplateUnmarshallerFor(theTypeKey).deserialize( theTemplate));
        }

        return theObject;
    }
}
