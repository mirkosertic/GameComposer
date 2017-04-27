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
package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.FutureResult;
import de.mirkosertic.gameengine.core.GameObject;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameSystemWork;
import de.mirkosertic.gameengine.core.Job;
import de.mirkosertic.gameengine.core.Logger;
import de.mirkosertic.gameengine.core.ThreadingManager;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventListener;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.PropertyChanged;
import de.mirkosertic.gameengine.physic.GameObjectCollision;
import de.mirkosertic.gameengine.physic.GamePhysicsManager;
import de.mirkosertic.gameengine.physic.PhysicsBehavior;
import de.mirkosertic.gameengine.physic.PhysicsBehaviorTemplate;
import de.mirkosertic.gameengine.physic.PhysicsDebugCanvas;
import de.mirkosertic.gameengine.physic.PlatformBehavior;
import de.mirkosertic.gameengine.physic.StaticBehavior;
import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.Force;
import de.mirkosertic.gameengine.type.Position;
import de.mirkosertic.gameengine.type.Size;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.Contact;

public class JBox2DGamePhysicsManager implements GamePhysicsManager {

    private class VisibleListener implements GameEventListener<PropertyChanged> {
        @Override
        public void handleGameEvent(PropertyChanged aEvent) {
            GameObjectInstance theInstance = (GameObjectInstance) aEvent.getOwner();
            Body theBody = staticObjects.get(theInstance);
            if (theBody == null) {
                theBody = dynamicObjects.get(theInstance);
            }
            if (theBody != null) {
                if (!theInstance.visibleProperty().isNull()) {
                    theBody.setActive(theInstance.visibleProperty().get());
                }
            }
        }
    }

    private class FixedAngleListener implements GameEventListener<PropertyChanged> {
        @Override
        public void handleGameEvent(PropertyChanged aEvent) {
            if (!insimulation) {
                PhysicsBehaviorTemplate thePhysicsComponentTemplate = (PhysicsBehaviorTemplate) aEvent.getOwner();
                for (Map.Entry<GameObjectInstance, Body> theEntry : dynamicObjects.entrySet()) {
                    if (theEntry.getKey().getOwnerGameObject() == thePhysicsComponentTemplate.getOwner()) {
                        theEntry.getValue().setFixedRotation(thePhysicsComponentTemplate.fixedRotationProperty().get());
                    }
                }
            }
        }
    }

    private class PositionChangeListener implements GameEventListener<PropertyChanged> {
        @Override
        public void handleGameEvent(PropertyChanged aEvent) {
            if (!insimulation) {
                //synchronized (physicsWorld) {
                    GameObjectInstance theInstance = (GameObjectInstance) aEvent.getOwner();
                    Body theBody = staticObjects.get(theInstance);
                    if (theBody == null) {
                        theBody = dynamicObjects.get(theInstance);
                    }
                    if (theBody != null) {
                        theBody.setTransform(computePosition(theInstance),
                                theInstance.rotationAngleProperty().get().invert().toRadians());
                    }
                //}
            }
        }
    }

    private class SizeChangeListener implements GameEventListener<PropertyChanged> {
        @Override
        public void handleGameEvent(PropertyChanged aEvent) {
            if (!insimulation) {
                GameObject theChangedObject = (GameObject) aEvent.getOwner();

                Set<GameObjectInstance> theChangedInstances = new HashSet<>();
                for (GameObjectInstance theObjectInstance : dynamicObjects.keySet()) {
                    if (theObjectInstance.getOwnerGameObject() == theChangedObject) {
                        theChangedInstances.add(theObjectInstance);
                    }
                }
                for (GameObjectInstance theObjectInstance : staticObjects.keySet()) {
                    if (theObjectInstance.getOwnerGameObject() == theChangedObject) {
                        theChangedInstances.add(theObjectInstance);
                    }
                }

                for (GameObjectInstance theInstance : theChangedInstances) {
                    Body theOldBody = gameObjectInstanceRemovedFromScene(theInstance);
                    Body theNewBody = gameObjectInstanceAddedToScene(theInstance);
                    if (theOldBody != null && theNewBody != null) {
                        theNewBody.setActive(theOldBody.isActive());
                    }
                }
            }
        }
    }

    private static final float SIZE_FACTOR = 0.01f;

    private final World physicsWorld;
    private final Map<GameObjectInstance, Body> dynamicObjects;
    private final Map<GameObjectInstance, Body> staticObjects;
    private long physicsAmountOfTime;
    private final GameEventManager eventManager;
    private final PositionChangeListener positionChangeListener;
    private final SizeChangeListener sizeChangeListener;
    private final FixedAngleListener fixedAngleListener;
    private final VisibleListener visibleListener;
    private final Set<GameObject> alreadyRegisteredSizeListener;
    private boolean insimulation;
    private final List<GameEvent> queuedEventsOfLastLoop;
    private final ThreadingManager threadingManager;
    private final Logger logger;

    JBox2DGamePhysicsManager(Logger aLogger, GameEventManager aEventManager, ThreadingManager aThreadingManager) {
        logger = aLogger;
        threadingManager = aThreadingManager;
        queuedEventsOfLastLoop = new ArrayList<>();
        eventManager = aEventManager;
        alreadyRegisteredSizeListener = new HashSet<>();
        positionChangeListener = new PositionChangeListener();
        sizeChangeListener = new SizeChangeListener();
        fixedAngleListener = new FixedAngleListener();
        visibleListener = new VisibleListener();

        // This is the gravity vector, it goes down in our coordinate system, of course
        Vec2 gravity = new Vec2(0.0f, -10.0f);
        physicsWorld = new World(gravity);
        physicsWorld.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact aContact) {
                Body theObjectA = aContact.getFixtureA().getBody();
                Body theObjectB = aContact.getFixtureB().getBody();
                queuedEventsOfLastLoop.add(new GameObjectCollision((GameObjectInstance) theObjectA.getUserData(),
                        (GameObjectInstance) theObjectB.getUserData()));
            }

            @Override
            public void endContact(Contact aContact) {
            }

            @Override
            public void preSolve(Contact aContact, Manifold aOldManifold) {
            }

            @Override
            public void postSolve(Contact aContact, ContactImpulse aImpulse) {
            }
        });
        dynamicObjects = new HashMap<>();
        staticObjects = new HashMap<>();
        physicsAmountOfTime = 0;
    }

    Body gameObjectInstanceRemovedFromScene(GameObjectInstance aInstance) {
        Body theSimulatedBody = dynamicObjects.remove(aInstance);
        aInstance.positionProperty().removeChangeListener(positionChangeListener);
        if (theSimulatedBody == null) {
            theSimulatedBody = staticObjects.remove(aInstance);
        }
        if (theSimulatedBody != null) {
            physicsWorld.destroyBody(theSimulatedBody);
            return theSimulatedBody;
        }
        return null;
    }

    Vec2 computePosition(GameObjectInstance aInstance) {
        Position thePosition = aInstance.positionProperty().get();
        Size theSize = aInstance.getOwnerGameObject().sizeProperty().get();

        return new Vec2(SIZE_FACTOR
                * (thePosition.x + theSize.width / 2), -SIZE_FACTOR
                * (thePosition.y + theSize.height / 2));
    }

    private BodyDef createBodyDefFor(GameObjectInstance aInstance, BodyType aBodyType) {
        BodyDef theBodyDef = new BodyDef();
        theBodyDef.userData = aInstance;
        theBodyDef.type = aBodyType;
        theBodyDef.angle = aInstance.rotationAngleProperty().get().invert().toRadians();
        theBodyDef.position = computePosition(aInstance);
        theBodyDef.gravityScale = 1;

        PhysicsBehaviorTemplate theTemplate = aInstance.getOwnerGameObject().getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE);
        if (theTemplate != null) {
            theBodyDef.active = theTemplate.activeProperty().get();
            theBodyDef.fixedRotation = theTemplate.fixedRotationProperty().get();
            theBodyDef.gravityScale = theTemplate.gravityScaleProperty().get();
        }

        return theBodyDef;
    }

    private FixtureDef createFixtureDefFor(GameObjectInstance aInstance, PolygonShape aShape) {
        FixtureDef theFixture = new FixtureDef();
        theFixture.shape = aShape;
        theFixture.density = 1;
        theFixture.friction = 1.8f;
        theFixture.restitution = 0;

        PhysicsBehaviorTemplate theTemplate = aInstance.getOwnerGameObject().getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE);
        if (theTemplate != null) {
            theFixture.density = theTemplate.densityProperty().get();
            theFixture.friction = theTemplate.frictionProperty().get();
            theFixture.restitution = theTemplate.restitutionProperty().get();
        }

        return theFixture;
    }

    Body gameObjectInstanceAddedToScene(GameObjectInstance aInstance) {
        //synchronized (physicsWorld) {
            aInstance.positionProperty().addChangeListener(positionChangeListener);
            aInstance.rotationAngleProperty().addChangeListener(positionChangeListener);
            aInstance.visibleProperty().addChangeListener(visibleListener);
            if (alreadyRegisteredSizeListener.add(aInstance.getOwnerGameObject())) {
                aInstance.getOwnerGameObject().sizeProperty().addChangeListener(sizeChangeListener);
                PhysicsBehaviorTemplate theTemplate = aInstance.getOwnerGameObject().getBehaviorTemplate(PhysicsBehaviorTemplate.TYPE);
                if (theTemplate != null) {
                    theTemplate.fixedRotationProperty().addChangeListener(fixedAngleListener);
                }
            }

            Size theInstanceSize = aInstance.getOwnerGameObject().sizeProperty().get();

            // Check if is a static component
            StaticBehavior theStaticComponent = aInstance.getBehavior(StaticBehavior.TYPE);
            if (theStaticComponent != null) {
                // The component is static hence not moveable, we add it as a ground to the physics simulation
                PolygonShape theStaticShape = new PolygonShape();
                theStaticShape.setAsBox(SIZE_FACTOR * theInstanceSize.width / 2, SIZE_FACTOR * theInstanceSize.height
                        / 2);

                FixtureDef theStaticFixture = createFixtureDefFor(aInstance, theStaticShape);
                BodyDef theBodyDef = createBodyDefFor(aInstance, BodyType.STATIC);
                Body theBody = physicsWorld.createBody(theBodyDef);
                theBody.createFixture(theStaticFixture);

                staticObjects.put(aInstance, theBody);

                return theBody;
            }

            // Now the player, hence the platform component
            PlatformBehavior thePlatformComponent = aInstance.getBehavior(PlatformBehavior.TYPE);
            if (thePlatformComponent != null) {
                PolygonShape thePlatformShape = new PolygonShape();
                thePlatformShape.setAsBox(SIZE_FACTOR * theInstanceSize.width / 2, SIZE_FACTOR * theInstanceSize.height
                        / 2);

                FixtureDef thePlatformFixture = createFixtureDefFor(aInstance, thePlatformShape);

                BodyDef theBodyDef = createBodyDefFor(aInstance, BodyType.DYNAMIC);
                Body theBody = physicsWorld.createBody(theBodyDef);
                dynamicObjects.put(aInstance, theBody);
                theBody.createFixture(thePlatformFixture);

                return theBody;
            }

            // Additional physics
            PhysicsBehavior thePhysicscomponent = aInstance.getBehavior(PhysicsBehavior.TYPE);
            if (thePhysicscomponent != null) {

                PolygonShape thePhysicsShape = new PolygonShape();
                thePhysicsShape.setAsBox(SIZE_FACTOR * theInstanceSize.width / 2, SIZE_FACTOR * theInstanceSize.height
                        / 2);

                FixtureDef thePhysicsFixture = createFixtureDefFor(aInstance, thePhysicsShape);

                BodyDef theBodyDef = createBodyDefFor(aInstance, BodyType.DYNAMIC);
                Body theBody = physicsWorld.createBody(theBodyDef);

                dynamicObjects.put(aInstance, theBody);
                theBody.createFixture(thePhysicsFixture);

                return theBody;
            }
        //}
        return null;
    }

    void applyImpulse(GameObjectInstance aInstance, Force aForce) {
        Body theBody = dynamicObjects.get(aInstance);
        if (theBody != null) {
            theBody.applyLinearImpulse(new Vec2(aForce.forceX, aForce.forceY), theBody.getWorldCenter());
        }
    }

    void applyForce(GameObjectInstance aInstance, Force aForce) {
        Body theBody = dynamicObjects.get(aInstance);
        if (theBody != null) {
            theBody.applyForce(new Vec2(aForce.forceX, aForce.forceY), theBody.getWorldCenter());
        }
    }

    void disableDynamicPhysicsOn(GameObjectInstance aInstance) {
        Body theSimulatedBody = dynamicObjects.get(aInstance);
        if (theSimulatedBody != null) {
            theSimulatedBody.setActive(false);
        }
    }

    void enableDynamicPhysicsOn(GameObjectInstance aInstance) {
        Body theSimulatedBody = dynamicObjects.get(aInstance);
        if (theSimulatedBody != null) {
            theSimulatedBody.setActive(true);
        }
    }

    public void updateGameObjectConfiguration(GameObject aGameObject) {
        for (GameObjectInstance theInstance : aGameObject.getGameScene().getInstances()) {
            if (theInstance.getOwnerGameObject() == aGameObject) {
                gameObjectInstanceRemovedFromScene(theInstance);
                gameObjectInstanceAddedToScene(theInstance);
            }
        }
    }

    private FutureResult lastResult;

    public GameSystemWork proceedGame(final long aTotalTicks, final long aGameTime, final long aElapsedTime) {
        return new GameSystemWork() {
            @Override
            public void runInFrame() {
                lastResult = threadingManager.submit(new Job() {
                    @Override
                    public void run() {
                        inframe(aTotalTicks, aGameTime, aElapsedTime);
                    }
                });
            }

            @Override
            public void runAfterFrame() {
                if (lastResult != null) {
                    try {
                        lastResult.waitForCompletion();
                        afterFrame();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        };
    }

    private void inframe(long aTotalTicks, long aGameTime, long aElapsedTime) {

        insimulation = true;

        physicsAmountOfTime += aElapsedTime;

        // We limit the physics system to 30 frames / second, or we are getting strange results
        if (physicsAmountOfTime >= 32) {
            // This define how accurately velocity will be simulated. Higher iteration value increases the accuracy
            // of velocity simulation but decreases the performance. The recommended velocity iteration value is 6.
            int theVelocityIterations = 6;

            // This is similar to velocity iteration, higher value means more accurate position simulation but
            // lesser performance. The recommended position iteration value is 3.
            int thePositionIterations = 2;

            float theTimestep = 1f / 30f;

            physicsWorld.step(theTimestep, theVelocityIterations, thePositionIterations);

            // Reset the time counter
            physicsAmountOfTime = 0;
        }

        insimulation = false;
    }

    private void afterFrame() {
        insimulation = true;

        // Fire the queued events
        // Events caused by the simulation(collision) are fired after the simulation
        // because events might change the world, and the world is locked
        // while running the simulation
        for (GameEvent theEvent : queuedEventsOfLastLoop) {
            eventManager.fire(theEvent);
        }
        queuedEventsOfLastLoop.clear();

        // Finally, we have to update the position of our game objects to sync them to the simulation
        for (Map.Entry<GameObjectInstance, Body> theEntry : dynamicObjects.entrySet()) {
            GameObjectInstance theObjectInstance = theEntry.getKey();
            Size theInstanceSize = theObjectInstance.getOwnerGameObject().sizeProperty().get();
            Body theSimulatedBody = theEntry.getValue();

            Vec2 thePosition = theSimulatedBody.getPosition();

            if (theSimulatedBody.isActive()) {
                // Now we have to use the XY coordinates again
                theObjectInstance.positionProperty().set(
                        new Position((thePosition.x / SIZE_FACTOR) - theInstanceSize.width / 2,
                                -(thePosition.y / SIZE_FACTOR) - theInstanceSize.height / 2));
                theObjectInstance.rotationAngleProperty().set(
                        Angle.fromRadians(theSimulatedBody.getAngle()).invert());
            }
        }

        insimulation = false;
    }

    private Position toPosition(Vec2 aVec) {
        return new Position(aVec.x / SIZE_FACTOR, -aVec.y / SIZE_FACTOR);
    }

    private Position toPosition(Vec2 aVec, Vec2 aOffset, float aAngleInRadians) {

        float theRotatedX = (float) (Math.cos(aAngleInRadians) * aVec.x - Math.sin(aAngleInRadians) * aVec.y);
        float theRotatedY = (float) (Math.sin(aAngleInRadians) * aVec.x + Math.cos(aAngleInRadians) * aVec.y);

        return new Position((theRotatedX + aOffset.x) / SIZE_FACTOR, -(theRotatedY + aOffset.y) / SIZE_FACTOR);
    }

    @Override
    public void drawDebug(PhysicsDebugCanvas aCanvas) {
        //synchronized (physicsWorld) {
            Body theBody = physicsWorld.getBodyList();
            while (theBody != null) {

                float theRotatedAngle = theBody.getAngle();

                Vec2 theBodyPosition = theBody.getPosition();
                aCanvas.drawPosition(toPosition(theBodyPosition));

                Fixture theFixture = theBody.getFixtureList();
                while (theFixture != null) {
                    Shape theShape = theFixture.getShape();
                    if (theShape.getType() == ShapeType.POLYGON) {
                        PolygonShape thePolyShape = (PolygonShape) theShape;
                        for (int i = 1; i < thePolyShape.getVertexCount(); i++) {
                            aCanvas.drawLine(
                                    toPosition(thePolyShape.getVertex(i - 1), theBodyPosition, theRotatedAngle),
                                    toPosition(thePolyShape.getVertex(i), theBodyPosition, theRotatedAngle),
                                    theBody.isAwake());
                        }
                        aCanvas.drawLine(
                                toPosition(thePolyShape.getVertex(thePolyShape.getVertexCount() - 1), theBodyPosition,
                                        theRotatedAngle),
                                toPosition(thePolyShape.getVertex(0), theBodyPosition, theRotatedAngle), theBody
                                .isAwake());
                    }
                    theFixture = theFixture.getNext();
                }

                theBody = theBody.getNext();
            }
        //}
    }
}