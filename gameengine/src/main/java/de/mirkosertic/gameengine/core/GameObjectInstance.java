package de.mirkosertic.gameengine.core;

import java.util.*;

public class GameObjectInstance {

    private Map<Class<GameComponent>, GameComponent> components;
    private Position position;
    private Size size;
    private String name;
    private GameObject ownerGameObject;

    GameObjectInstance(GameObject aOwnerGameObject) {
        ownerGameObject = aOwnerGameObject;
        components = new HashMap<>();
        position = new Position();
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Size getSize() {
        return size;
    }

    public void setSize(Size size) {
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        HashSet<GameComponent> theResult = new HashSet<>(components.values());
        return Collections.unmodifiableSet(theResult);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("gameobjectuuid", ownerGameObject.getUuid());
        theResult.put("size", size.serializeToMap());
        theResult.put("position", position.serializeToMap());
        theResult.put("name", name);

        List<Map<String, Object>> theComponents = new ArrayList<>();
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
        theResult.size = Size.deserialize((Map<String, Object>) theInstance.get("size"));
        theResult.position = Position.deserialize((Map<String, Object>) theInstance.get("position"));
        theResult.name = (String) theInstance.get("name");

        List<Map<String, Object>> theComponents = (List<Map<String, Object>>) theInstance.get("components");
        for (Map<String, Object> theStructure : theComponents) {
            String theType = (String) theStructure.get(GameComponent.TYPE_ATTRIBUTE);

            GameComponentUnmarshaller theUnmarshaller = aGameRuntime.getComponentUnmarshallerFor(theType);
            theResult.addComponent(theUnmarshaller.deserialize(aGameRuntime, theResult, theStructure));
        }

        return theResult;
    }
}
