package de.mirkosertic.gameengine.physics;

import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.processes.AbstractGameProcess;
import de.mirkosertic.gameengine.types.Angle;

public class ConstantMovementProcess extends AbstractGameProcess {

    private final ConstantMovement movement;
    private final GameRuntime gameRuntime;

    ConstantMovementProcess(GameRuntime aRuntime, ConstantMovement aMovement) {
        movement = aMovement;
        gameRuntime = aRuntime;
    }

    @Override
    public ProceedResult proceedGame(long aGameTime, long aElapsedTimeSinceLastLoop) {
        GameObjectInstance theInstance = movement.getObjectInstance();
        Angle theAngle = movement.movementDirectionProperty().get();
        float theImpulse = movement.movementForceProperty().get();
        float theRadians = theAngle.toRadians();
        float theImpulseX = (float) (Math.cos(theRadians) * theImpulse / 1000 * aElapsedTimeSinceLastLoop);
        float theImpulseY = (float) (Math.sin(theRadians) * theImpulse / 1000 * aElapsedTimeSinceLastLoop);
        gameRuntime.getEventManager().fire(new ApplyImpulseToGameObjectInstance(theInstance, theImpulseX, theImpulseY));

        return ProceedResult.CONTINUE_RUNNING;
    }
}
