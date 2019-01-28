/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
import org.luaj.vm2.LuaString;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.compiler.LuaC;

public class LuaJScriptEngineFactory implements LUAScriptEngineFactory {

    private static class CacheEntry {

        private final Prototype prototype;
        private final Globals globals;
        private final Map<String, LuaJScriptEngine> engines;

        public CacheEntry(Prototype aPrototype, Globals aGlobals) {
            prototype = aPrototype;
            globals = aGlobals;
            engines = new HashMap<>();
        }

        public LuaJScriptEngine getScriptEngineFor(KeyValueObjectCache aCache, String aMethodName) {
            LuaJScriptEngine theEngine = engines.get(aMethodName);
            if (theEngine == null) {
                theEngine = new LuaJScriptEngine(aCache, globals, aMethodName);
                engines.put(aMethodName, theEngine);
            }
            return theEngine;
        }
    }

    private final Map<String, CacheEntry> prototypes;

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

        CacheEntry theCacheEntry = prototypes.get(aScriptCode);
        if (theCacheEntry == null) {

            Globals theGlobals = createGlobals(aScene);
            Prototype thePrototype = theGlobals.compilePrototype(new StringReader(aScriptCode), "script");

            // Initialize the globals and the code
            LuaClosure theClosure = new LuaClosure(thePrototype, theGlobals);
            theClosure.call();

            theCacheEntry = new CacheEntry(thePrototype, theGlobals);
            prototypes.put(aScriptCode, theCacheEntry);
        }

        return theCacheEntry.getScriptEngineFor(aScene, aMethodName);
    }
}