package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.AbsolutePositionAnchor;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectInstance implements Reflectable<GameObjectInstanceClassInformation> {

    public static final String UUID_PROPERTY = "uuid";
    public static final String NAME_PROPERTY = "name";
    public static final String POSITION_PROPERTY = "position";
    public static final String VISIBLE_PROPERTY = "visible";
    public static final String ROTATION_ANGLE_PROPERTY = "rotationAngle";
    public static final String ABSOLUTE_POSITION_PROPERTY = "absolutePosition";
    public static final String ABSOLUTE_POSITION_ANCHOR_PROPERTY = "absolutePositionAnchor";

    private final Map<String, Behavior> components;

    private final GameObject ownerGameObject;

    final Property<String> uuid;
    final Property<String> name;
    final Property<Position> position;
    final Property<Angle> rotationAngle;
    final Property<Boolean> visible;
    final Property<Boolean> absolutePosition;
    final Property<AbsolutePositionAnchor> absolutePositionAnchor;

    GameObjectInstance(GameEventManager aEventManager, GameObject aOwnerGameObject) {

        uuid = new Property<>(String.class, this, UUID_PROPERTY, de.mirkosertic.gameengine.type.UUID.randomUID(), aEventManager);
        name = new Property<>(String.class, this, NAME_PROPERTY, aEventManager);
        position = new Property<>(Position.class, this, POSITION_PROPERTY, new Position(), aEventManager);
        visible = new Property<>(Boolean.class, this, VISIBLE_PROPERTY, aOwnerGameObject.visibleProperty().get(), aEventManager);
        rotationAngle = new Property<>(Angle.class, this, ROTATION_ANGLE_PROPERTY, new Angle(0), aEventManager);
        absolutePosition = new Property<>(Boolean.class, this, ABSOLUTE_POSITION_PROPERTY, Boolean.FALSE, aEventManager);
        absolutePositionAnchor = new Property<>(AbsolutePositionAnchor.class, this, ABSOLUTE_POSITION_ANCHOR_PROPERTY, AbsolutePositionAnchor.TOP_LEFT, aEventManager);

        ownerGameObject = aOwnerGameObject;
        components = new HashMap<>();
    }

    @Override
    public GameObjectInstanceClassInformation getClassInformation() {
        return GameObjectInstanceClassInformation.INSTANCE;
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

    void addComponent(Behavior aComponent) {
        components.put(aComponent.getClass().getSimpleName(), aComponent);
    }

    public <T extends Behavior> T getComponent(Class<T> aComponentClass) {
        return (T) components.get(aComponentClass.getSimpleName());
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        if (!uuidProperty().isNull()) {
            theResult.put(UUID_PROPERTY, uuidProperty().get());
        }
        theResult.put("gameobjectuuid", ownerGameObject.uuidProperty().get());

        theResult.put(POSITION_PROPERTY, position.get().serialize());
        theResult.put(NAME_PROPERTY, name.get());
        theResult.put(VISIBLE_PROPERTY, Boolean.toString(visible.get()));
        theResult.put(ABSOLUTE_POSITION_PROPERTY, Boolean.toString(absolutePosition.get()));
        theResult.put(ABSOLUTE_POSITION_ANCHOR_PROPERTY, absolutePositionAnchor.get().name());
        theResult.put("rotationangle", rotationAngle.get().serialize());

        List<Map<String, Object>> theComponents = new ArrayList<>();
        for (Behavior theComponent : components.values()) {
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
            String theType = (String) theStructure.get(Behavior.TYPE_ATTRIBUTE);

            BehaviorUnmarshaller theUnmarshaller = aGameRuntime.getIORegistry().getBehaviorUnmarshallerFor(theType);
            theResult.addComponent(theUnmarshaller.deserialize(aGameRuntime, theResult, theStructure));
        }

        return theResult;
    }
}
