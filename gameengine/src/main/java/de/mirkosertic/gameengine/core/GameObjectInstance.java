package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.event.PropertyAware;
import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectInstance extends PropertyAware {

    public static final String UUID_PROPERTY = "uuid";
    public static final String NAME_PROPERTY = "name";
    public static final String POSITION_PROPERTY = "position";
    public static final String VISIBLE_PROPERTY = "visible";
    public static final String ROTATION_ANGLE_PROPERTY = "rotationAngle";
    public static final String ABSOLUTE_POSITION_PROPERTY = "absolutePosition";
    public static final String ABSOLUTE_POSITION_ANCHOR_PROPERTY = "absolutePositionAnchor";

    private final Map<Class<GameComponent>, GameComponent> components;

    private final GameObject ownerGameObject;

    private final Property<String> uuid;
    private final Property<String> name;
    private final Property<Position> position;
    private final Property<Angle> rotationAngle;
    private final Property<Boolean> visible;
    private final Property<Boolean> absolutePosition;
    private final Property<AbsolutePositionAnchor> absolutePositionAnchor;

    GameObjectInstance(GameEventManager aEventManager, GameObject aOwnerGameObject) {

        uuid = registerProperty(new Property<String>(this, UUID_PROPERTY, de.mirkosertic.gameengine.type.UUID.randomUID(), aEventManager));
        name = registerProperty(new Property<String>(this, NAME_PROPERTY, aEventManager));
        position = registerProperty(new Property<Position>(this, POSITION_PROPERTY, new Position(), aEventManager));
        visible = registerProperty(new Property<Boolean>(this, VISIBLE_PROPERTY, aOwnerGameObject.visibleProperty().get(), aEventManager));
        rotationAngle = registerProperty(new Property<Angle>(this, ROTATION_ANGLE_PROPERTY, new Angle(0), aEventManager));
        absolutePosition = registerProperty(new Property<Boolean>(this, ABSOLUTE_POSITION_PROPERTY, Boolean.FALSE, aEventManager));
        absolutePositionAnchor = registerProperty(new Property<AbsolutePositionAnchor>(this, ABSOLUTE_POSITION_ANCHOR_PROPERTY, AbsolutePositionAnchor.TOP_LEFT, aEventManager));

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

    public Property<Boolean> absolutePositionProperty() {
        return absolutePosition;
    }

    public Property<AbsolutePositionAnchor> absolutePositionAnchorProperty() {
        return absolutePositionAnchor;
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

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        if (!uuidProperty().isNull()) {
            theResult.put(UUID_PROPERTY,  uuidProperty().get());
        }
        theResult.put("gameobjectuuid", ownerGameObject.uuidProperty().get());

        theResult.put(POSITION_PROPERTY, position.get().serialize());
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put(VISIBLE_PROPERTY, Boolean.toString(visible.get()));
        theResult.put(ABSOLUTE_POSITION_PROPERTY, Boolean.toString(absolutePosition.get()));
        theResult.put(ABSOLUTE_POSITION_ANCHOR_PROPERTY, absolutePositionAnchor.get().name());
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
        theResult.position.setQuietly(Position.deserialize((Map<String, Object>) theInstance.get(POSITION_PROPERTY)));
        theResult.name.setQuietly((String) theInstance.get(NAME_PROPERTY));

        String theInstanceUUID = (String) theInstance.get(UUID_PROPERTY);
        if (theInstanceUUID != null) {
            theResult.uuid.setQuietly(theInstanceUUID);
        }
        String theVisible = (String) theInstance.get(VISIBLE_PROPERTY);
        if (theVisible != null) {
            theResult.visible.setQuietly(Boolean.parseBoolean(theVisible));
        }
        String theAbsolutePositon = (String) theInstance.get(ABSOLUTE_POSITION_PROPERTY);
        if (theAbsolutePositon != null) {
            theResult.absolutePosition.setQuietly(Boolean.parseBoolean(theAbsolutePositon));
        }
        String theAnchor = (String) theInstance.get(ABSOLUTE_POSITION_ANCHOR_PROPERTY);
        if (theAnchor != null) {
            theResult.absolutePositionAnchor.setQuietly(AbsolutePositionAnchor.valueOf(theAnchor));
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
