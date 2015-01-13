package de.mirkosertic.gameengine.scriptengine.luaj;

import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
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

    private Map<String, Prototype> prototypes;

    private final Reflectable buildInFunctions;

    public LuaJScriptEngineFactory(Reflectable aBuildInFunctions) {
        prototypes = new HashMap<>();
        buildInFunctions = aBuildInFunctions;
    }

    private Globals createGlobals() {
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

        LuaJScriptEngine.registerTo(theGlobals, buildInFunctions);


        return theGlobals;
    }

    @Override
    public LuaJScriptEngine createNewEngine(Script aScript) throws IOException {
        return create(aScript.script, "proceedGame");
    }

    @Override
    public LuaJScriptEngine createNewEngine(TextExpression aExpression) throws IOException {

        StringBuilder theScriptCode = new StringBuilder("function process(aInstance) return ");
        theScriptCode.append(aExpression.expression);
        theScriptCode.append(" end");

        return create(theScriptCode.toString(), "process");
    }

    private LuaJScriptEngine create(String aScriptCode, String aMethodName) throws IOException {

        Globals theGlobals = createGlobals();

        Prototype thePrototype = prototypes.get(aScriptCode);
        if (thePrototype == null) {
            thePrototype = theGlobals.compilePrototype(new StringReader(aScriptCode), "script");
            prototypes.put(aScriptCode, thePrototype);
        }

        return new LuaJScriptEngine(theGlobals, new LuaClosure(thePrototype, theGlobals), aMethodName);
    }
}