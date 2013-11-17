package de.mirkosertic.gameengine.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.types.Size;

public class GameObject extends PropertyAware {

    private final GameScene gameScene;

    private final Property<String> uuid;
    private final Property<String> name;
    private final Property<Size> size;

    private final Map<Class<GameComponentTemplate>, GameComponentTemplate> componentTemplates;

    GameObject(GameScene aScene, String aName) {
        this(aScene, aName, de.mirkosertic.gameengine.types.UUID.randomUID());
    }

    GameObject(GameScene aScene, String aName, String aUUID) {

        GameEventManager theManager = aScene.getRuntime().getEventManager();

        gameScene = aScene;
        uuid = registerProperty(new Property<String>(this, "uuid", aUUID, theManager));
        name = registerProperty(new Property<String>(this, "name", aName, theManager));
        size = registerProperty(new Property<Size>(this, "size", new Size(64, 64), theManager));
        componentTemplates = new HashMap<Class<GameComponentTemplate>, GameComponentTemplate>();

        name.setQuietly(aName);
        uuid.setQuietly(aUUID);
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    public Property<String> uuidProperty() {
        return uuid;
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<Size> sizeProperty() {
        return size;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObject that = (GameObject) o;

        return !(uuid != null ? !uuid.equals(that.uuid) : that.uuid != null);

    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }

    @Override
    public boolean setPropertyByName(String aPropertyName, Object aPropertyValue) {
        for (GameComponentTemplate theTemplate : componentTemplates.values()) {
            if (theTemplate.setPropertyByName(aPropertyName, aPropertyValue)) {
                return true;
            }
        }
        return super.setPropertyByName(aPropertyName, aPropertyValue);
    }

    public void add(GameComponentTemplate aComponentTemplate) {
        componentTemplates.put((Class<GameComponentTemplate>) aComponentTemplate.getClass(), aComponentTemplate);
        gameScene.getRuntime().getEventManager().fire(new GameObjectConfigurationChanged(this));
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
        theResult.put("name", name.get());
        theResult.put("uuid", uuid.get());
        theResult.put("size", size.get().serializeToMap());
        List<Map<String, Object>> theTemplates = new ArrayList<Map<String, Object>>();
        for (GameComponentTemplate theTemplate : componentTemplates.values()) {
           theTemplates.add(theTemplate.serialize());
        }
        theResult.put("templates", theTemplates);
        return theResult;
    }

    public static GameObject deserialize(GameRuntime aGameRuntime, GameScene aGameScene, Map<String, Object> theSerializedData) {
        String theName = (String) theSerializedData.get("name");
        String theUUID = (String) theSerializedData.get("uuid");
        GameObject theObject = new GameObject(aGameScene, theName, theUUID);

        Map<String, Object> theSize = (Map<String, Object>) theSerializedData.get("size");
        if (theSize != null) {
            theObject.size.setQuietly(Size.deserialize(theSize));
        }

        List<Map<String, Object>> theTemplates = (List<Map<String, Object>>) theSerializedData.get("templates");
        for (Map<String, Object> theTemplate : theTemplates) {
            String theTypeKey = (String) theTemplate.get(GameComponent.TYPE_ATTRIBUTE);
            theObject.add(aGameRuntime.getIORegistry().getTemplateUnmarshallerFor(theTypeKey).deserialize(aGameRuntime.getEventManager(), theObject, theTemplate));
        }

        return theObject;
    }
}