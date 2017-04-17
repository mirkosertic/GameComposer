/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.mirkosertic.gameengine.core;

import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.PositionAnchor;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameObjectInstance implements Reflectable<GameObjectInstanceClassInformation> {

    private static final GameObjectInstanceClassInformation CIINSTANCE = new GameObjectInstanceClassInformation();

    public static final String UUID_PROPERTY = "uuid";
    public static final String NAME_PROPERTY = "name";
    public static final String POSITION_PROPERTY = "position";
    public static final String VISIBLE_PROPERTY = "visible";
    public static final String ROTATION_ANGLE_PROPERTY = "rotationAngle";
    public static final String POSITION_ANCHOR_PROPERTY = "positionAnchor";

    private final Map<BehaviorType, Behavior> behaviors;

    private final GameObject ownerGameObject;

    private final Property<String> uuid;
    private final Property<String> name;
    private final Property<Position> position;
    private final Property<Angle> rotationAngle;
    private final Property<Boolean> visible;
    private final Property<PositionAnchor> positionAnchor;

    private boolean remoteObject;

    GameObjectInstance(GameEventManager aEventManager, GameObject aOwnerGameObject) {

        uuid = new Property<>(String.class, this, UUID_PROPERTY, de.mirkosertic.gameengine.type.UUID.randomUID(), aEventManager);
        name = new Property<>(String.class, this, NAME_PROPERTY, aEventManager);
        position = new Property<>(Position.class, this, POSITION_PROPERTY, new Position(), aEventManager);
        visible = new Property<>(Boolean.class, this, VISIBLE_PROPERTY, aOwnerGameObject.visibleProperty().get(), aEventManager);
        rotationAngle = new Property<>(Angle.class, this, ROTATION_ANGLE_PROPERTY, new Angle(0), aEventManager);
        positionAnchor = new Property<>(PositionAnchor.class, this, POSITION_ANCHOR_PROPERTY, PositionAnchor.SCENE, aEventManager);

        ownerGameObject = aOwnerGameObject;
        behaviors = new HashMap<>();
    }

    @Override
    public GameObjectInstanceClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @ReflectiveField
    public Property<String> uuidProperty() {
        return uuid;
    }

    @ReflectiveField
    public Property<Position> positionProperty() {
        return position;
    }

    @ReflectiveField
    public Property<String> nameProperty() {
        return name;
    }

    @ReflectiveField
    public Property<Angle> rotationAngleProperty() {
        return rotationAngle;
    }

    @ReflectiveField
    public Property<Boolean> visibleProperty() {
        return visible;
    }

    @ReflectiveField
    public Property<PositionAnchor> positionAnchorProperty() {
        return positionAnchor;
    }

    public GameObject getOwnerGameObject() {
        return ownerGameObject;
    }

    void addBehavior(Behavior aBehavior) {
        behaviors.put(aBehavior.getType(), aBehavior);
    }

    public void removeBehavior(Behavior aBehavior) {
        behaviors.remove(aBehavior.getType());
    }

    public void removeBehaviorByTemplate(BehaviorTemplate aTemplate) {
        Map<BehaviorType, Behavior> theKnownBehaviors = new HashMap<>(behaviors);
        for (Map.Entry<BehaviorType, Behavior> theEntry : theKnownBehaviors.entrySet()) {
            if (theEntry.getValue().getTemplate() == aTemplate) {
                removeBehavior(theEntry.getValue());
            }
        }
    }

    @ReflectiveMethod
    public Behavior findBehaviorByType(String aType) {
        for (Map.Entry<BehaviorType, Behavior> theEntry : behaviors.entrySet()) {
            if (aType.equals(theEntry.getValue().getType().getValue())) {
                return theEntry.getValue();
            }
        }
        return null;
    }

    public <T extends Behavior> T getBehavior(BehaviorType aBehaviorID) {
        return (T) behaviors.get(aBehaviorID);
    }

    public void markAsRemoteObject() {
        for (Behavior theBehavior : behaviors.values()) {
            theBehavior.markAsRemoteObject();
        }
        remoteObject = true;
    }

    public boolean isRemoteObject() {
        return remoteObject;
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
        theResult.put(POSITION_ANCHOR_PROPERTY, positionAnchor.get().name());
        theResult.put("rotationangle", rotationAngle.get().serialize());

        List<Map<String, Object>> theComponents = new ArrayList<>();
        for (Behavior theComponent : behaviors.values()) {
            theComponents.add(theComponent.serialize());
        }
        theResult.put("components", theComponents);

        return theResult;
    }

    public static GameObjectInstance deserialize(GameRuntime aGameRuntime, GameScene aScene, Map<String, Object> theInstance) {

        String theGameObjectUUID = (String) theInstance.get("gameobjectuuid");
        GameObject theGameObject = aScene.findObjectByID(theGameObjectUUID);
        if (theGameObject == null) {
            throw new RuntimeException("Cannot find gameobject with uuid " + theGameObjectUUID);
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

        String theAbsolutePositon = (String) theInstance.get("absolutePosition");
        if (theAbsolutePositon != null) {
            if (!Boolean.parseBoolean(theAbsolutePositon)) {
                theResult.positionAnchor.setQuietly(PositionAnchor.SCENE);
            } else {
                String theAnchor = (String) theInstance.get("absolutePositionAnchor");
                if (theAnchor != null) {
                    theResult.positionAnchor.setQuietly(PositionAnchor.valueOf(theAnchor));
                } else {
                    theResult.positionAnchor.setQuietly(PositionAnchor.SCENE);
                }
            }
        } else {
            String theAnchor = (String) theInstance.get(POSITION_ANCHOR_PROPERTY);
            if (theAnchor != null) {
                theResult.positionAnchor.setQuietly(PositionAnchor.valueOf(theAnchor));
            } else {
                theResult.positionAnchor.setQuietly(PositionAnchor.SCENE);
            }
        }

        Map<String, Object> theRotationAngle = (Map<String, Object>) theInstance.get("rotationangle");
        if (theRotationAngle != null) {
            theResult.rotationAngle.setQuietly(Angle.deserialize(theRotationAngle));
        }

        List<Map<String, Object>> theComponents = (List<Map<String, Object>>) theInstance.get("components");
        for (Map<String, Object> theStructure : theComponents) {
            BehaviorType theType = new BehaviorType((String) theStructure.get(Behavior.TYPE_ATTRIBUTE));

            BehaviorUnmarshaller theUnmarshaller = aGameRuntime.getIORegistry().getBehaviorUnmarshallerFor(theType);
            theResult.addBehavior(theUnmarshaller.deserialize(aGameRuntime, theResult, theStructure));
        }
        return theResult;
    }
}