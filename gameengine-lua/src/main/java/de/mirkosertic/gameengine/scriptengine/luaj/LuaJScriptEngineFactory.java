package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;

import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Reflectable;
import de.mirkosertic.gameengine.type.Script;
import de.mirkosertic.gameengine.type.TextExpression;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;

public class LuaJScriptEngineFactory implements LUAScriptEngineFactory {

    private final Map<String, Prototype> prototypes;

    private final Reflectable buildInFunctions;

    public LuaJScriptEngineFactory(Reflectable aBuildInFunctions) {
        prototypes = new HashMap<>();
        buildInFunctions = aBuildInFunctions;
    }

    private Globals createGlobals(KeyValueObjectCache aObjectCache) {
        Globals theGlobals = new Globals();
//         globals.load(new BaseLib());
//        globals.load(new PackageLib());
//        globals.load(new Bit32Lib());
//        globals.load(new OsLib());
//        globals.load(new MathLib());
//        globals.load(new TableLib());
//        globals.load(new StringLib());
//        globals.load(new CoroutineLib());
//        globals.load(new JmeIoLib());
//        LoadState.install(globals);
        LuaC.install(theGlobals);

        LuaJScriptEngine.registerTo(theGlobals, buildInFunctions, aObjectCache);


        return theGlobals;
    }

    @Override
    public LuaJScriptEngine createNewEngine(KeyValueObjectCache aScene, Script aScript) throws IOException {
        return create(aScene, aScript.script, "proceedGame");
    }

    @Override
    public LuaJScriptEngine createNewEngine(KeyValueObjectCache aScene, TextExpression aExpression) throws IOException {

        StringBuilder theScriptCode = new StringBuilder("function process(instance, scene, game) return ");
        theScriptCode.append(aExpression.expression);
        theScriptCode.append(" end");

        return create(aScene, theScriptCode.toString(), "process");
    }

    private LuaJScriptEngine create(KeyValueObjectCache aScene, String aScriptCode, String aMethodName) throws IOException {

        Globals theGlobals = createGlobals(aScene);

        Prototype thePrototype = prototypes.get(aScriptCode);
        if (thePrototype == null) {
            thePrototype = theGlobals.compilePrototype(new StringReader(aScriptCode), "script");
            prototypes.put(aScriptCode, thePrototype);
        }

        // Initialize the globals and the code
        new LuaClosure(thePrototype, theGlobals).call();

        return new LuaJScriptEngine(aScene, theGlobals, aMethodName);
    }
}