package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.types.*;

import java.util.*;

public class GameObjectInstance extends PropertyAware {

    private final Map<Class<GameComponent>, GameComponent> components;

    private final GameObject ownerGameObject;

    private final Property<String> uuid;
    private final Property<String> name;
    private final Property<Position> position;
    private final Property<Angle> rotationAngle;
    private final Property<Boolean> visible;

    GameObjectInstance(GameEventManager aEventManager, GameObject aOwnerGameObject) {

        uuid = registerProperty(new Property<String>(this, "uuid", de.mirkosertic.gameengine.types.UUID.randomUID(), aEventManager));
        name = registerProperty(new Property<String>(this, "name", aEventManager));
        position = registerProperty(new Property<Position>(this, "position", new Position(), aEventManager));
        visible = registerProperty(new Property<Boolean>(this, "visible", aOwnerGameObject.visibleProperty().get(), aEventManager));
        rotationAngle = registerProperty(new Property<Angle>(this, "rotationAngle", new Angle(0), aEventManager));

        ownerGameObject = aOwnerGameObject;
        components = new HashMap<Class<GameComponent>, GameComponent>();
    }

    public boolean contains(Position aPosition) {
        Size theSize = ownerGameObject.sizeProperty().get();
        Position thePosition = position.get();
        return (aPosition.x >= thePosition.x && aPosition.y >= thePosition.y &&
                aPosition.x <= thePosition.x + theSize.width &&
                aPosition.y <= thePosition.y + theSize.height);
    }

    public Property<String> uuidProperty() {
        return uuid;
    }

    public Property<Position> positionProperty() {
        return position;
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<Angle> rotationAngleProperty() {
        return rotationAngle;
    }

    public Property<Boolean> visibleProperty() {
        return visible;
    }

    public GameObject getOwnerGameObject() {
        return ownerGameObject;
    }

    void addComponent(GameComponent aComponent) {
        components.put((Class<GameComponent>) aComponent.getClass(), aComponent);
    }

    public <T extends GameComponent> T getComponent(Class<T> aComponentClass) {
        return (T) components.get(aComponentClass);
    }

    @Override
    public boolean setPropertyByName(String aPropertyName, Object aPropertyValue) {
        for (GameComponent theComponent : components.values()) {
            if (theComponent.setPropertyByName(aPropertyName, aPropertyValue)) {
                return true;
            }
        }
        return super.setPropertyByName(aPropertyName, aPropertyValue);
    }

    public Set<GameComponent> getComponents() {
        HashSet<GameComponent> theResult = new HashSet<GameComponent>(components.values());
        return Collections.unmodifiableSet(theResult);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        if (!uuidProperty().isNull()) {
            theResult.put("uuid",  uuidProperty().get());
        }
        theResult.put("gameobjectuuid", ownerGameObject.uuidProperty().get());

        theResult.put("position", position.get().serializeToMap());
        theResult.put("name", name.get());
        theResult.put("visible", Boolean.toString(visible.get()));
        theResult.put("rotationangle", rotationAngle.get().serialize());

        List<Map<String, Object>> theComponents = new ArrayList<Map<String, Object>>();
        for (GameComponent theComponent : components.values()) {
            theComponents.add(theComponent.serialize());
        }
        theResult.put("components", theComponents);

        return theResult;
    }

    public static GameObjectInstance deserialize(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> theInstance) {

        String theGameObjectUUID = (String) theInstance.get("gameobjectuuid");
        GameObject theGameObject = aScene.findGameObjectByID(theGameObjectUUID);
        if (theGameObject == null) {
            throw new RuntimeException("Cannot find gameobject with uuid" + theGameObjectUUID);
        }

        GameObjectInstance theResult = new GameObjectInstance(aGameRuntime.getEventManager(), theGameObject);
        theResult.position.setQuietly(Position.deserialize((Map<String, Object>) theInstance.get("position")));
        theResult.name.setQuietly((String) theInstance.get("name"));

        String theInstanceUUID = (String) theInstance.get("uuid");
        if (theInstanceUUID != null) {
            theResult.uuid.setQuietly(theInstanceUUID);
        }
        String theVisible = (String) theInstance.get("visible");
        if (theVisible != null) {
            theResult.visible.setQuietly(Boolean.parseBoolean(theVisible));
        }

        Map<String, Object> theRotationAngle = (Map<String, Object>) theInstance.get("rotationangle");
        if (theRotationAngle != null) {
            theResult.rotationAngle.setQuietly(Angle.deserialize(theRotationAngle));
        }

        List<Map<String, Object>> theComponents = (List<Map<String, Object>>) theInstance.get("components");
        for (Map<String, Object> theStructure : theComponents) {
            String theType = (String) theStructure.get(GameComponent.TYPE_ATTRIBUTE);

            GameComponentUnmarshaller theUnmarshaller = aGameRuntime.getIORegistry().getComponentUnmarshallerFor(theType);
            theResult.addComponent(theUnmarshaller.deserialize(aGameRuntime, theResult, theStructure));
        }

        return theResult;
    }
}
