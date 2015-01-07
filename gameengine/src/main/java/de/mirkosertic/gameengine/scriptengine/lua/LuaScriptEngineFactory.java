package de.mirkosertic.gameengine.scriptengine.lua;

import de.mirkosertic.gameengine.scriptengine.ScriptEngineFactory;
import de.mirkosertic.gameengine.type.Script;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;

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

        Globals theGlobals = createGlobals();

        String theScriptCode = aScript.script;
        Prototype thePrototype = prototypes.get(theScriptCode);
        if (thePrototype == null) {
            thePrototype = theGlobals.compilePrototype(new StringReader(theScriptCode), "script");
            prototypes.put(theScriptCode, thePrototype);
        }

        return new LuaScriptEngine(theGlobals, new LuaClosure(thePrototype, theGlobals));
    }
}