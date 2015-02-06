package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
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

    private static final GameObjectClassInformation CIINSTANCE = new GameObjectClassInformation();

    public static final String UUID_PROPERTY = "uuid";
    public static final String NAME_PROPERTY = "name";
    public static final String SIZE_PROPERTY = "size";
    public static final String VISIBLE_PROPERTY = "visible";

    private final GameScene gameScene;

    private final Property<String> uuid;
    private final Property<String> name;
    private final Property<Size> size;
    private final Property<Boolean> visible;

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
    }

    public GameScene getGameScene() {
        return gameScene;
    }

    @ReflectiveField
    public Property<String> uuidProperty() {
        return uuid;
    }

    @ReflectiveField
    public Property<String> nameProperty() {
        return name;
    }

    @ReflectiveField
    public Property<Size> sizeProperty() {
        return size;
    }

    @ReflectiveField
    public Property<Boolean> visibleProperty() {
        return visible;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameObject that = (GameObject) o;

        return uuid.get().equals(that.uuid.get());
    }

    @Override
    public int hashCode() {
        return uuid.get().hashCode();
    }

    public void add(BehaviorTemplate aBehaviorTemplate) {
        componentTemplates.put((Class<BehaviorTemplate>) aBehaviorTemplate.getClass(), aBehaviorTemplate);
        gameScene.getRuntime().getEventManager().fire(new GameObjectConfigurationChanged(this));
    }

    public void remove(BehaviorTemplate aBehaviorTemplate) {
        componentTemplates.remove(aBehaviorTemplate.getClass());
        gameScene.getRuntime().getEventManager().fire(new GameObjectConfigurationChanged(this));
    }

    public <T extends BehaviorTemplate> T getBehaviorTemplate(Class<T> aBehaviorClass) {
        return (T) componentTemplates.get(aBehaviorClass);
    }

    @Override
    public GameObjectClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    public Set<BehaviorTemplate> getBehaviorTemplates() {
        HashSet<BehaviorTemplate> theResult = new HashSet<>();
        theResult.addAll(componentTemplates.values());
        return theResult;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put(UUID_PROPERTY, uuid.get());
        theResult.put(VISIBLE_PROPERTY, Boolean.toString(visible.get()));
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

        String theVisible = (String) theSerializedData.get(VISIBLE_PROPERTY);
        if (theVisible != null) {
            theObject.visibleProperty().setQuietly(Boolean.valueOf(theVisible));
        }

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