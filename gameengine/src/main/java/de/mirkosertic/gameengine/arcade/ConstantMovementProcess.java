package de.mirkosertic.gameengine.arcade;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.physics.ApplyImpulseToGameObjectInstance;
import de.mirkosertic.gameengine.processes.AbstractGameProcess;
import de.mirkosertic.gameengine.types.Angle;

public class ConstantMovementProcess extends AbstractGameProcess {

    private final GameObjectInstance instance;
    private final Angle angle;
    private final float force;
    private final GameRuntime gameRuntime;

    ConstantMovementProcess(GameRuntime aRuntime, GameObjectInstance aInstance, Angle aAngle, float aForce) {
        gameRuntime = aRuntime;
        instance = aInstance;
        angle = aAngle;
        force = aForce;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        float theRadians = angle.toRadians();
        float theImpulseX = (float) (Math.cos(theRadians) * force / 1000 * aElapsedTimeSinceLastLoop);
        float theImpulseY = (float) (Math.sin(theRadians) * force / 1000 * aElapsedTimeSinceLastLoop);
        gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(instance, theImpulseX, theImpulseY));

        return ProceedResult.CONTINUE_RUNNING;
    }

    @Override
    public boolean affectsInstance(GameObjectInstance aInstance) {
        return instance.uuidProperty().get().equals(aInstance.uuidProperty().get());
    }
}