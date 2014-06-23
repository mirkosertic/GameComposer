package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameObject implements Reflectable<GameObjectClassInformation> {

    public static final String UUID_PROPERTY = "uuid";
    public static final String NAME_PROPERTY = "name";
    public static final String SIZE_PROPERTY = "size";
    public static final String VISIBLE_PROPERTY = "visible";

    private final GameScene gameScene;

    final Property<String> uuid;
    final Property<String> name;
    final Property<Size> size;
    final Property<Boolean> visible;

    private final Map<Class<BehaviorTemplate>, BehaviorTemplate> componentTemplates;

    public GameObject(GameScene aScene, String aName) {
        this(aScene, aName, de.mirkosertic.gameengine.type.UUID.randomUID());
    }

    GameObject(GameScene aScene, String aName, String aUUID) {

        GameEventManager theManager = aScene.getRuntime().getEventManager();

        gameScene = aScene;
        uuid = new Property<>(String.class, this, UUID_PROPERTY, aUUID, theManager);
        name = new Property<>(String.class, this, NAME_PROPERTY, aName, theManager);
        size = new Property<>(Size.class, this, SIZE_PROPERTY, new Size(64, 64), theManager);
        visible = new Property<>(Boolean.class, this, VISIBLE_PROPERTY, Boolean.TRUE, theManager);
        componentTemplates = new HashMap<>();

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

    public Property<Boolean> visibleProperty() {
        return visible;
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

    public void add(BehaviorTemplate aComponentTemplate) {
        componentTemplates.put((Class<BehaviorTemplate>) aComponentTemplate.getClass(), aComponentTemplate);
        gameScene.getRuntime().getEventManager().fire(new GameObjectConfigurationChanged(this));
    }

    public <T extends BehaviorTemplate> T getComponentTemplate(Class<T> aComponentClass) {
        return (T) componentTemplates.get(aComponentClass);
    }

    @Override
    public GameObjectClassInformation getClassInformation() {
        return GameObjectClassInformation.INSTANCE;
    }

    public Set<BehaviorTemplate> getComponentTemplates() {
        HashSet<BehaviorTemplate> theResult = new HashSet<>();
        theResult.addAll(componentTemplates.values());
        return theResult;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put(UUID_PROPERTY, uuid.get());
        theResult.put(SIZE_PROPERTY, size.get().serialize());
        List<Map<String, Object>> theTemplates = new ArrayList<>();
        for (BehaviorTemplate theTemplate : componentTemplates.values()) {
           theTemplates.add(theTemplate.serialize());
        }
        theResult.put("templates", theTemplates);
        return theResult;
    }

    public static GameObject deserialize(GameRuntime aGameRuntime, GameScene aGameScene, Map<String, Object> theSerializedData) {
        String theName = (String) theSerializedData.get(NAME_PROPERTY);
        String theUUID = (String) theSerializedData.get(UUID_PROPERTY);
        GameObject theObject = new GameObject(aGameScene, theName, theUUID);

        Map<String, Object> theSize = (Map<String, Object>) theSerializedData.get(SIZE_PROPERTY);
        if (theSize != null) {
            theObject.size.setQuietly(Size.deserialize(theSize));
        }

        List<Map<String, Object>> theTemplates = (List<Map<String, Object>>) theSerializedData.get("templates");
        for (Map<String, Object> theTemplate : theTemplates) {
            String theTypeKey = (String) theTemplate.get(Behavior.TYPE_ATTRIBUTE);
            theObject.add(aGameRuntime.getIORegistry().getBehaviorTemplateUnmarshallerFor(theTypeKey).deserialize(aGameRuntime.getEventManager(), theObject, theTemplate));
        }

        return theObject;
    }
}