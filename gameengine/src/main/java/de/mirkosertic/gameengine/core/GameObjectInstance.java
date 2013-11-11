package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.types.Angle;
import de.mirkosertic.gameengine.types.Position;
import de.mirkosertic.gameengine.types.Size;

import java.util.*;

public class GameObjectInstance {

    private Map<Class<GameComponent>, GameComponent> components;

    private GameObject ownerGameObject;

    private Property<Position> position;
    private Property<String> name;
    private Property<Angle> rotationAngle;

    GameObjectInstance(GameObject aOwnerGameObject) {

        GameEventManager theManager = aOwnerGameObject.getGameScene().getRuntime().getEventManager();

        name = new Property<String>(this, "name", theManager);
        position = new Property<Position>(this, "position", new Position(), theManager);
        rotationAngle = new Property<Angle>(this, "rotationAngle", new Angle(0), theManager);

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

    public Property<Position> positionProperty() {
        return position;
    }

    public Property<String> nameProperty() {
        return name;
    }

    public Property<Angle> rotationAngleProperty() {
        return rotationAngle;
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

    public Set<GameComponent> getComponents() {
        HashSet<GameComponent> theResult = new HashSet<GameComponent>(components.values());
        return Collections.unmodifiableSet(theResult);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("gameobjectuuid", ownerGameObject.uuidProperty().get());

        theResult.put("position", position.get().serializeToMap());
        theResult.put("name", name.get());
        theResult.put("rotationangle", rotationAngle.get().serialize());

        List<Map<String, Object>> theComponents = new ArrayList<Map<String, Object>>();
        for (GameComponent theComponent : components.values()) {
            theComponents.add(theComponent.serialize());
        }
        theResult.put("components", theComponents);

        return theResult;
    }

    public static GameObjectInstance deserialize(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> theInstance) {

        String theUUID = (String) theInstance.get("gameobjectuuid");
        GameObject theGameObject = aScene.findGameObjectByID(theUUID);
        if (theGameObject == null) {
            throw new RuntimeException("Cannot find gameobject with uuid" + theUUID);
        }

        GameObjectInstance theResult = new GameObjectInstance(theGameObject);
        theResult.position.setQuietly(Position.deserialize((Map<String, Object>) theInstance.get("position")));
        theResult.name.setQuietly((String) theInstance.get("name"));

        Map<String, Object> theRotationAngle = (Map<String, Object>) theInstance.get("rotationangle");
        if (theRotationAngle != null) {
            theResult.rotationAngle.setQuietly(Angle.deserialize(theRotationAngle));
        }

        List<Map<String, Object>> theComponents = (List<Map<String, Object>>) theInstance.get("components");
        for (Map<String, Object> theStructure : theComponents) {
            String theType = (String) theStructure.get(GameComponent.TYPE_ATTRIBUTE);

            GameComponentUnmarshaller theUnmarshaller = aGameRuntime.getComponentUnmarshallerFor(theType);
            theResult.addComponent(theUnmarshaller.deserialize(aGameRuntime, theResult, theStructure));
        }

        return theResult;
    }
}
