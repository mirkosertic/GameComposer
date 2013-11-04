package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.GameEventManager;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.Position;
import de.mirkosertic.gameengine.core.Size;
import de.mirkosertic.gameengine.physics.*;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.collision.shapes.Shape;
import org.jbox2d.collision.shapes.ShapeType;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.HashMap;
import java.util.Map;

public class JBox2DGamePhysicsManager implements GamePhysicsManager {

    private static final float SIZE_FACTOR = 0.01f;

    private World physicsWorld;
    private Map<GameObjectInstance, Body> dynamicObjects;
    private Map<GameObjectInstance, Body> staticObjects;
    private long physicsAmountOfTime;
    private GameEventManager eventManager;

    JBox2DGamePhysicsManager(GameEventManager aEventManager) {
        eventManager = aEventManager;

        // This is the gravity vector, it goes down in our coordinate system, of course
        Vec2 gravity = new Vec2(0.0f, -10.0f);
        physicsWorld = new World(gravity);
        physicsWorld.setContactListener(new ContactListener() {
            public void beginContact(Contact aContact) {
                Body theObjectA = aContact.getFixtureA().getBody();
                Body theObjectB = aContact.getFixtureB().getBody();
                eventManager.fire(new GameObjectCollisionEvent((GameObjectInstance) theObjectA.getUserData(), (GameObjectInstance) theObjectB.getUserData()));
            }

            public void endContact(Contact aContact) {
            }

            public void preSolve(Contact aContact, Manifold aOldManifold) {
            }

            public void postSolve(Contact aContact, ContactImpulse aImpulse) {
            }
        });
        dynamicObjects = new HashMap<GameObjectInstance, Body>();
        staticObjects = new HashMap<GameObjectInstance, Body>();
        physicsAmountOfTime = 0;
    }

    void gameObjectInstanceRemovedFromScene(GameObjectInstance aInstance) {
        synchronized (physicsWorld) {
            Body theSimulatedBody = dynamicObjects.remove(aInstance);
            if (theSimulatedBody == null) {
                theSimulatedBody = staticObjects.remove(aInstance);
            }
            if (theSimulatedBody != null) {
                int count = physicsWorld.getBodyCount();
                physicsWorld.destroyBody(theSimulatedBody);
            }
        }
    }

    void gameObjectInstancePositionChanged(GameObjectInstance aInstance, Position aNewPosition) {
        gameObjectInstanceRemovedFromScene(aInstance);
        gameObjectInstanceAddedToScene(aInstance);
    }

    void gameObjectInstanceAddedToScene(GameObjectInstance aInstance) {
        synchronized (physicsWorld) {
            Position theInstancePosition = aInstance.getPosition();
            Size theInstanceSize = aInstance.getSize();

            // Check if is a static component
            StaticComponent theStaticComponent = aInstance.getComponent(StaticComponent.class);
            if (theStaticComponent != null) {
                // The component is static hence not moveable, we add it as a ground to the physics simulation
                PolygonShape theStaticShape = new PolygonShape();
                theStaticShape.setAsBox(SIZE_FACTOR * theInstanceSize.width / 2, SIZE_FACTOR * theInstanceSize.height / 2);

                FixtureDef theStaticFixture = new FixtureDef();
                theStaticFixture.shape = theStaticShape;
                theStaticFixture.density = 1;
                theStaticFixture.friction = 1.8f;
                theStaticFixture.restitution = 0;

                BodyDef theStaticBodyDef = new BodyDef();
                theStaticBodyDef.type = BodyType.STATIC;
                theStaticBodyDef.userData = aInstance;
                // The position is the CENTER of MASS, not the X/Y Coordinate
                theStaticBodyDef.position = new Vec2(SIZE_FACTOR * (theInstancePosition.x + theInstanceSize.width / 2), -SIZE_FACTOR * (theInstancePosition.y + theInstanceSize.height / 2));
                Body theBody = physicsWorld.createBody(theStaticBodyDef);
                theBody.createFixture(theStaticFixture);

                staticObjects.put(aInstance, theBody);
            }

            // Now the player, hence the platform component
            PlatformComponent thePlatformComponent = aInstance.getComponent(PlatformComponent.class);
            if (thePlatformComponent != null) {
                PolygonShape thePlatformShape = new PolygonShape();
                thePlatformShape.setAsBox(SIZE_FACTOR * theInstanceSize.width / 2, SIZE_FACTOR * theInstanceSize.height / 2);

                FixtureDef thePlatformFixture = new FixtureDef();
                thePlatformFixture.density = 1;
                thePlatformFixture.friction = 0.1f;
                thePlatformFixture.restitution = 0;
                thePlatformFixture.shape = thePlatformShape;

                BodyDef thePlatformBodyDef = new BodyDef();
                thePlatformBodyDef.type = BodyType.DYNAMIC;
                // The position is the CENTER of MASS, not the X/Y Coordinate
                thePlatformBodyDef.position = new Vec2(SIZE_FACTOR * (theInstancePosition.x + theInstanceSize.width / 2), SIZE_FACTOR * (theInstancePosition.y + theInstanceSize.height / 2));
                thePlatformBodyDef.userData = aInstance;

                Body thePlatformBody = physicsWorld.createBody(thePlatformBodyDef);
                dynamicObjects.put(aInstance, thePlatformBody);
                thePlatformBody.createFixture(thePlatformFixture);
            }
        }
    }

    void applyImpulse(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        synchronized (physicsWorld) {
            Body theBody = dynamicObjects.get(aInstance);
            if (theBody != null) {
                theBody.applyLinearImpulse(new Vec2(aImpulseX, aImpulseY), theBody.getWorldCenter());
            }
        }
    }

    void applyForce(GameObjectInstance aInstance, float aForceX, float aForceY) {
        synchronized (physicsWorld) {
            Body theBody = dynamicObjects.get(aInstance);
            if (theBody != null) {
                theBody.applyForce(new Vec2(aForceX, aForceY), theBody.getWorldCenter());
            }
        }
    }

    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

        synchronized (physicsWorld) {
            physicsAmountOfTime += aElapsedTimeSinceLastLoop;

            // We limit the physics system to 60 frames / second, or we are getting strange results
            if (physicsAmountOfTime > 16) {
                // This define how accurately velocity will be simulated. Higher iteration value increases the accuracy of velocity simulation but decreases the performance. The recommended velocity iteration value is 6.
                int theVelocityIterations = 6;

                // This is similar to velocity iteration, higher value means more accurate position simulation but lesser performance. The recommended position iteration value is 3.
                int thePositionIterations = 2;

                float theTimestep = 1f / 60f * physicsAmountOfTime / 16;

                // The time measure of JBox2D is 1/60 seconds
                physicsWorld.step(theTimestep, theVelocityIterations, thePositionIterations);

                // Finally, we have to update the position of our game objects to sync them to the simulation
                for (Map.Entry<GameObjectInstance, Body> theEntry : dynamicObjects.entrySet()) {
                    GameObjectInstance theGameObject = theEntry.getKey();
                    Size theInstanceSize = theGameObject.getSize();
                    Body theSimulatedBody = theEntry.getValue();

                    Vec2 thePosition = theSimulatedBody.getPosition();

                    // Now we have to use the XY coordinates again
                    theGameObject.setPosition(new Position((thePosition.x / SIZE_FACTOR) - theInstanceSize.width / 2, -(thePosition.y / SIZE_FACTOR) - theInstanceSize.height / 2));
                }

                // Reset the time counter
                physicsAmountOfTime = 0;
            }
        }
    }

    private Position toPosition(Vec2 aVec) {
        return new Position(aVec.x / SIZE_FACTOR, -aVec.y / SIZE_FACTOR);
    }

    private Position toPosition(Vec2 aVec, Vec2 aOffset) {
        return new Position((aVec.x + aOffset.x) / SIZE_FACTOR, -(aVec.y + aOffset.y) / SIZE_FACTOR);
    }


    @Override
    public void drawDebug(PhysicsDebugCanvas aCanvas) {
        synchronized (physicsWorld) {
            Body theBody = physicsWorld.getBodyList();
            while (theBody != null) {

                Vec2 theBodyPosition = theBody.getPosition();
                aCanvas.drawPosition(toPosition(theBodyPosition));

                Fixture theFixture = theBody.getFixtureList();
                while (theFixture != null) {
                    Shape theShape = theFixture.getShape();
                    if (theShape.getType() == ShapeType.POLYGON) {
                        PolygonShape thePolyShape = (PolygonShape) theShape;
                        for (int i = 1; i < thePolyShape.getVertexCount(); i++) {
                            aCanvas.drawLine(toPosition(thePolyShape.getVertex(i - 1), theBodyPosition), toPosition(thePolyShape.getVertex(i), theBodyPosition));
                        }
                        aCanvas.drawLine(toPosition(thePolyShape.getVertex(thePolyShape.getVertexCount() - 1), theBodyPosition), toPosition(thePolyShape.getVertex(0), theBodyPosition));
                    }
                    theFixture = theFixture.getNext();
                }

                theBody = theBody.getNext();
            }
        }
    }
}