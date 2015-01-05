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

    public static final String TYPE_VALUE = "RunScriptAction";
    public static final String SCRIPT_PROPERTY = "script";

    private final Property<Script> script;

    @UsedByReflection
    public RunScriptAction() {
        script = new Property<>(Script.class, this, SCRIPT_PROPERTY, new Script(""));
    }

    @Override
    public void invoke(GameScene aScene, ConditionResult aResult) {
        Script theScript = script.get();
        GameEventManager theManager = aScene.getRuntime().getEventManager();
        ScriptEngineFactory theScriptEngineFactory = aScene.getRuntime().getScriptEngineFactory();
        for (GameObjectInstance theInstance : aResult.getAffectedInstances()) {
            ScriptProcess theProcess = new ScriptProcess(theInstance, theScriptEngineFactory, theScript);
            theManager.fire(new StartProcess(theProcess));
        }
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put(TYPE_ATTRIBUTE, TYPE_VALUE);
        theResult.put("script", script.get());
        return theResult;
    }

    public static RunScriptAction unmarshall(Map<String, Object> aSerializedData) {
        RunScriptAction theResult = new RunScriptAction();
        String theScript = (String) aSerializedData.get("script");
        if (theScript != null) {
            theResult.script.setQuietly(new Script(theScript));
        }
        return theResult;
    }
}
