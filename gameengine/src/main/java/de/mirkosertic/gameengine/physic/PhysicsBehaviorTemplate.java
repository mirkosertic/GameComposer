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
package de.mirkosertic.gameengine.physic;

import de.mirkosertic.gameengine.core.BehaviorTemplate;
import de.mirkosertic.gameengine.core.BehaviorType;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Reflectable;

import java.util.HashMap;
import java.util.Map;

public class PhysicsBehaviorTemplate implements BehaviorTemplate<PhysicsBehavior>, Physics, Reflectable<PhysicsClassInformation> {

    public static final BehaviorType TYPE = new BehaviorType("ID");

    private static final PhysicsClassInformation CIINSTANCE = new PhysicsClassInformation();

    private final GameObject owner;

    private final Property<Boolean> active;
    private final Property<Boolean> fixedRotation;
    private final Property<Float> density;
    private final Property<Float> friction;
    private final Property<Float> restitution;
    private final Property<Float> gravityScale;

    public PhysicsBehaviorTemplate(GameEventManager aEventManager, GameObject aOwner) {
        owner = aOwner;

        active = new Property<>(Boolean.class, this, ACTIVE_PROPERTY, Boolean.TRUE, aEventManager);
        fixedRotation = new Property<>(Boolean.class, this, FIXED_ROTATION_PROPERTY, Boolean.FALSE, aEventManager);
        density = new Property<>(Float.class, this, DENSITY_PROPERTY, 1f, aEventManager);
        friction = new Property<>(Float.class, this, FRICTION_PROPERTY, 1.8f, aEventManager);
        restitution = new Property<>(Float.class, this, RESTITUTION_PROPERTY, 0f, aEventManager);
        gravityScale = new Property<>(Float.class, this, GRAVITY_SCALE_PROPERTY, 1f, aEventManager);
    }

    @Override
    public BehaviorType getType() {
        return TYPE;
    }

    @Override
    public PhysicsClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public GameObject getOwner() {
        return owner;
    }

    @Override
    public Property<Boolean> activeProperty() {
        return active;
    }

    @Override
    public Property<Boolean> fixedRotationProperty() {
        return fixedRotation;
    }

    @Override
    public Property<Float> densityProperty() {
        return density;
    }

    @Override
    public Property<Float> frictionProperty() {
        return friction;
    }

    @Override
    public Property<Float> restitutionProperty() {
        return restitution;
    }

    @Override
    public Property<Float> gravityScaleProperty() {
        return gravityScale;
    }

    @Override
    public PhysicsBehavior create(GameObjectInstance aInstance, GameRuntime aGameRuntime) {
        return new PhysicsBehavior(aInstance, this);
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(PhysicsBehavior.TYPE_ATTRIBUTE, PhysicsBehavior.TYPE);
        theResult.put(ACTIVE_PROPERTY, Boolean.toString(active.get()));
        theResult.put("fixedrotation", Boolean.toString(fixedRotation.get()));
        theResult.put(DENSITY_PROPERTY, Float.toString(density.get()));
        theResult.put(FRICTION_PROPERTY, Float.toString(friction.get()));
        theResult.put(RESTITUTION_PROPERTY, Float.toString(restitution.get()));
        theResult.put(GRAVITY_SCALE_PROPERTY, Float.toString(gravityScale.get()));
        return theResult;
    }

    public static PhysicsBehaviorTemplate deserialize(GameEventManager aEventManager, GameObject aOwner, Map<String, Object> aSerializedData) {
        PhysicsBehaviorTemplate theTemplate = new PhysicsBehaviorTemplate(aEventManager, aOwner);
        String theActive = (String) aSerializedData.get(ACTIVE_PROPERTY);
        if (theActive != null) {
            theTemplate.active.setQuietly(Boolean.parseBoolean(theActive));
        }
        String theFixedRotation = (String) aSerializedData.get("fixedrotation");
        if (theFixedRotation != null) {
            theTemplate.fixedRotation.setQuietly(Boolean.parseBoolean(theFixedRotation));
        }
        String theDensity = (String) aSerializedData.get(DENSITY_PROPERTY);
        if (theDensity != null) {
            theTemplate.density.setQuietly(Float.parseFloat(theDensity));
        }
        String theFriction = (String) aSerializedData.get(FRICTION_PROPERTY);
        if (theFriction != null) {
            theTemplate.friction.setQuietly(Float.parseFloat(theFriction));
        }
        String theRestitution = (String) aSerializedData.get(RESTITUTION_PROPERTY);
        if (theRestitution != null) {
            theTemplate.restitution.setQuietly(Float.parseFloat(theRestitution));
        }
        String theGravityScale = (String) aSerializedData.get(GRAVITY_SCALE_PROPERTY);
        if (theGravityScale != null) {
            theTemplate.gravityScale.setQuietly(Float.parseFloat(theGravityScale));
        }

        return theTemplate;
    }

    @Override
    public void delete() {
        owner.getGameScene().removeBehaviorFrom(owner, this);
    }
}