/*
 * Copyright 2019 Mirko Sertic
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
package de.mirkosertic.gameengine.bytecoder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.mirkosertic.bytecoder.unittest.BytecoderUnitTestRunner;
import de.mirkosertic.gameengine.script.RunScriptAction;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngine;
import de.mirkosertic.gameengine.scriptengine.LUAScriptEngineFactory;
import de.mirkosertic.gameengine.scriptengine.luaj.LuaJScriptEngineFactory;
import de.mirkosertic.gameengine.type.KeyValueObjectCache;
import de.mirkosertic.gameengine.type.Script;

@RunWith(BytecoderUnitTestRunner.class)
public class ScriptEngineTest {

    public static final String SCRIPT = "--\n"
            + "--\n"
            + "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop) \n"
            + "\n"
            + "\t-- The method must return either STOPPED or CONTINUE_RUNNING\n"
            + "\t-- STOPPED will kill the script, it is not invoked the next time\n"
            + "\t-- CONTINUE_RUNNING will invoke the skript the next game loop cycle.\n"
            + "\treturn emptyString()\n"
            + "end";

    public static final String EMPTY = "function proceedGame(aGameTime, aElapsedTimeSinceLastLoop)\nreturn 'STOPPED'\nend";

    @Test
    public void testScriptEngine() throws IOException {
        LUAScriptEngineFactory theFactory = (LUAScriptEngineFactory) new LuaJScriptEngineFactory(new BytecoderBuildInFunctions());
        Map<Object, Object> cache = new HashMap<>();
        KeyValueObjectCache theCache = new KeyValueObjectCache() {
            @Override
            public <T> T getObjectForKey(Object aKey) {
                return (T) cache.get(aKey);
            }

            @Override
            public <T> void setObjectForKey(Object aKey, T aValue) {
                cache.put(aKey, aValue);
            }
        };
        LUAScriptEngine theEngine = theFactory.createNewEngine(theCache, new Script(EMPTY));
        String theResult = (String) theEngine.proceedGame(100, 200);
        Assert.assertEquals("STOPPED", theResult);;
    }
}
