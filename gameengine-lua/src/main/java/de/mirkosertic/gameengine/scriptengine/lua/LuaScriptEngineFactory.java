package de.mirkosertic.gameengine.scriptengine.lua;

import de.mirkosertic.gameengine.scriptengine.ScriptEngine;
import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;
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
import org.luaj.vm2.lib.BaseLib;

public class LuaScriptEngineFactory implements ScriptEngineFactory {

    private Map<String, Prototype> prototypes;

    public LuaScriptEngineFactory() {
        prototypes = new HashMap<>();
    }

    private Globals createGlobals() {
        Globals globals = new Globals();
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
        LuaC.install(globals);
        return globals;
    }

    @Override
    public LuaScriptEngine createNewEngine(Script aScript) throws IOException {
        return create(aScript.script, "proceedGame");
    }

    @Override
    public LuaScriptEngine createNewEngine(TextExpression aExpression) throws IOException {

        StringBuilder theScriptCode = new StringBuilder("function process(aInstance) return ");
        theScriptCode.append(aExpression.expression);
        theScriptCode.append(" end");

        return create(theScriptCode.toString(), "process");
    }

    private LuaScriptEngine create(String aScriptCode, String aMethodName) throws IOException {

        Globals theGlobals = createGlobals();

        Prototype thePrototype = prototypes.get(aScriptCode);
        if (thePrototype == null) {
            thePrototype = theGlobals.compilePrototype(new StringReader(aScriptCode), "script");
            prototypes.put(aScriptCode, thePrototype);
        }

        return new LuaScriptEngine(theGlobals, new LuaClosure(thePrototype, theGlobals), aMethodName);
    }
}