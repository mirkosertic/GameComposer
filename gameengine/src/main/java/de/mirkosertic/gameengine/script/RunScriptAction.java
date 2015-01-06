package de.mirkosertic.gameengine.script;

import de.mirkosertic.gameengine.core.Action;
import de.mirkosertic.gameengine.core.ConditionResult;
import de.mirkosertic.gameengine.core.GameObjectInstance;
import de.mirkosertic.gameengine.core.GameScene;
import de.mirkosertic.gameengine.core.UsedByReflection;
import de.mirkosertic.gameengine.event.GameEventManager;
import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.process.StartProcess;
import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;
import de.mirkosertic.gameengine.type.Script;

import java.util.HashMap;
import java.util.Map;

public class RunScriptAction implements Action {

    private static final String DEFAULT_SCRIPT = "--\n"
            + "-- This is the default method of every action. It is called once for every game loop cycle.\n"
            + "--\n"
            + "-- instance is an optional injected object which is a reference to the GameObjectInstance for \n"
            + "-- which this script is executed.\n"
            + "-- scene is an injected object which is a reference to the GameScene for \n"
            + "-- which this script is executed.\n"
            + "--\n"
            + "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) \n"
            + "\n"
            + "\t-- The method must return either STOPPED or CONTINUE_RUNNING\n"
            + "\t-- STOPPED will kill the script, it is not invoked the next time\n"
            + "\t-- CONTINUE_RUNNING will invoke the skript the next game loop cycle.\n"
            + "\treturn 'STOPPED'\n"
            + "end";

    public static final String TYPE_VALUE = "RunScriptAction";
    public static final String SCRIPT_PROPERTY = "script";

    private final Property<Script> script;

    @UsedByReflection
    public RunScriptAction() {
        script = new Property<>(Script.class, this, SCRIPT_PROPERTY, new Script(DEFAULT_SCRIPT));
    }

    public Property<Script> scriptProperty() {
        return script;
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        Script theScript = script.get();
        GameEventManager theManager = aScene.getRuntime().getEventManager();
        ScriptEngineFactory theScriptEngineFactory = aScene.getRuntime().getScriptEngineFactory();
        int theNumberOfProcesses = 0;
        GameObjectInstance[] theInstances = aResult.getAffectedInstances();
        // Spawn only instance specific script instances if there are not all instances selected...
        if (theInstances.length < aScene.getInstances().length) {
            for (GameObjectInstance theInstance : theInstances) {
                ScriptProcess theProcess = new ScriptProcess(theInstance, theScriptEngineFactory, theScript);
                theManager.fire(new StartProcess(theProcess));
                theNumberOfProcesses++;
            }
        }
        if (theNumberOfProcesses == 0) {
            // No instance specific processes created, so we will spawn a scene based process
            ScriptProcess theProcess = new ScriptProcess(aScene, theScriptEngineFactory, theScript);
            theManager.fire(new StartProcess(theProcess));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("script", script.get().serialize());
        return theResult;
    }

    public static RunScriptAction unmarshall(Map<String, Object> aSerializedData) {
        RunScriptAction theResult = new RunScriptAction();
        Script theScript = Script.deserialize((Map<String, Object>) aSerializedData.get("script"));
        theResult.script.setQuietly(theScript);
        return theResult;
    }
}