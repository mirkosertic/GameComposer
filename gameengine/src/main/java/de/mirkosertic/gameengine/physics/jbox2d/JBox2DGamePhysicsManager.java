package de.mirkosertic.gameengine.physics.jbox2d;

import de.mirkosertic.gameengine.core.*;
import de.mirkosertic.gameengine.physics.GameObjectCollisionEvent;
import de.mirkosertic.gameengine.physics.GamePhysicsManager;
import de.mirkosertic.gameengine.physics.PlatformComponent;
import de.mirkosertic.gameengine.physics.StaticComponent;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.jbox2d.dynamics.contacts.Contact;

import java.util.HashMap;
import java.util.Map;

public class JBox2DGamePhysicsManager implements GamePhysicsManager {

    private static final float SIZE_FACTOR = 0.01f;

    private World physicsWorld;
    private Map<GameObjectInstance, Body> simulatedObjects;
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
        simulatedObjects = new HashMap<GameObjectInstance, Body>();
        physicsAmountOfTime = 0;
    }

    void gameObjectInstanceAddedToScene(GameObjectInstance aInstance) {
        Position theInstancePosition = aInstance.getPosition();
        Size theInstanceSize = aInstance.getSize();

        // Check if is a static component
        StaticComponent theStaticComponent = aInstance.getComponent(StaticComponent.class);
        if (theStaticComponent != null) {
            // The component is static hence not moveable, we add it as a ground to the physics simulation
            PolygonShape theStaticShape = new PolygonShape();
            theStaticShape.setAsBox(SIZE_FACTOR * theInstanceSize.getWidth() / 2, SIZE_FACTOR * theInstanceSize.getHeight() / 2);

            FixtureDef theStaticFixture = new FixtureDef();
            theStaticFixture.shape = theStaticShape;
            theStaticFixture.density = 1;
            theStaticFixture.friction = 1.8f;
            theStaticFixture.restitution = 0;

            BodyDef theStaticBodyDef = new BodyDef();
            theStaticBodyDef.type = BodyType.STATIC;
            theStaticBodyDef.userData = aInstance;
            // The position is the CENTER of MASS, not the X/Y Coordinate
            theStaticBodyDef.position = new Vec2(SIZE_FACTOR * (theInstancePosition.getX() + theInstanceSize.getWidth() / 2), - SIZE_FACTOR * (theInstancePosition.getY() + theInstanceSize.getHeight() / 2));
            physicsWorld.createBody(theStaticBodyDef).createFixture(theStaticFixture);
        }

        // Now the player, hence the platform component
        PlatformComponent thePlatformComponent = aInstance.getComponent(PlatformComponent.class);
        if (thePlatformComponent != null) {
            PolygonShape thePlatformShape = new PolygonShape();
            thePlatformShape.setAsBox(SIZE_FACTOR * theInstanceSize.getWidth() / 2, SIZE_FACTOR * theInstanceSize.getHeight() / 2);

            FixtureDef thePlatformFixture = new FixtureDef();
            thePlatformFixture.density = 1;
            thePlatformFixture.friction = 0.1f;
            thePlatformFixture.restitution = 0;
            thePlatformFixture.shape = thePlatformShape;

            BodyDef thePlatformBodyDef = new BodyDef();
            thePlatformBodyDef.type = BodyType.DYNAMIC;
            // The position is the CENTER of MASS, not the X/Y Coordinate
            thePlatformBodyDef.position = new Vec2(SIZE_FACTOR * (theInstancePosition.getX() + theInstanceSize.getWidth() / 2), SIZE_FACTOR * (theInstancePosition.getY() + theInstanceSize.getHeight() / 2));
            thePlatformBodyDef.userData = aInstance;

            Body thePlatformBody = physicsWorld.createBody(thePlatformBodyDef);
            simulatedObjects.put(aInstance, thePlatformBody);
            thePlatformBody.createFixture(thePlatformFixture);
        }
    }

    void applyImpulse(GameObjectInstance aInstance, float aImpulseX, float aImpulseY) {
        Body theBody = simulatedObjects.get(aInstance);
        if (theBody != null) {
            theBody.applyLinearImpulse(new Vec2(aImpulseX, aImpulseY), theBody.getWorldCenter());
        }
    }

    void applyForce(GameObjectInstance aInstance, float aForceX, float aForceY) {
        Body theBody = simulatedObjects.get(aInstance);
        if (theBody != null) {
            theBody.applyForce(new Vec2(aForceX, aForceY), theBody.getWorldCenter());
        }
    }

    public void proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {

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
            for (Map.Entry<GameObjectInstance, Body> theEntry : simulatedObjects.entrySet()) {
                GameObjectInstance theGameObject = theEntry.getKey();
                Size theInstanceSize = theGameObject.getSize();
                Body theSimulatedBody = theEntry.getValue();

                Vec2 thePosition = theSimulatedBody.getPosition();

                // Now we have to use the XY coordinates again
                theGameObject.setPosition(new Position((thePosition.x / SIZE_FACTOR) - theInstanceSize.getWidth() / 2, - (thePosition.y / SIZE_FACTOR) - theInstanceSize.getHeight() / 2));
            }

            // Reset the time counter
            physicsAmountOfTime = 0;
        }
    }

    public Vec2 getForceOn(GameObjectInstance aInstance) {
        Body theBody = simulatedObjects.get(aInstance);
        if (theBody != null) {
            return theBody.getLinearVelocity();
        }
        return null;
    }

    public boolean isAwake(GameObjectInstance aInstance) {
        Body theBody = simulatedObjects.get(aInstance);
        return theBody != null && theBody.isAwake();
    }
}