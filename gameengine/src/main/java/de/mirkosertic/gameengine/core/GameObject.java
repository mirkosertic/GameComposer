package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Size;

import java.util.*;

import static de.mirkosertic.gameengine.event.PropertyFactory.createSizeProperty;
import static de.mirkosertic.gameengine.event.PropertyFactory.createStringProperty;

public class GameObject {

    private GameScene gameScene;

    private Property<String> uuid;
    private Property<String> name;
    private Property<Size> size;

    private Map<Class<GameComponentTemplate>, GameComponentTemplate> componentTemplates;

    public GameObject(GameScene aScene, String aName) {
        this(aScene, aName, de.mirkosertic.gameengine.types.UUID.randomUID());
    }

    GameObject(GameScene aScene, String aName, String aUUID) {
        gameScene = aScene;
        uuid = createStringProperty(this, "uuid", aUUID, aScene.getRuntime().getEventManager());
        name = createStringProperty(this, "name", aName, aScene.getRuntime().getEventManager());
        size = createSizeProperty(this, "size", new Size(64, 64), aScene.getRuntime().getEventManager());
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

    public void add(GameComponentTemplate aComponentTemplate) {
        componentTemplates.put((Class<GameComponentTemplate>) aComponentTemplate.getClass(), aComponentTemplate);
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
            theObject.add(aGameRuntime.getTemplateUnmarshallerFor(theTypeKey).deserialize(theObject, theTemplate));
        }

        return theObject;
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
}