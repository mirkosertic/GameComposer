/*
 * Copyright 2017 Mirko Sertic
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
package de.mirkosertic.gameengine.teavmwasm;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Test;

public class JSONParserTest {

    @Test
    public void testEmpty() {
        Map<String, Object> theResult = new JSONParser().fromJSON("{}");
        assertEquals(0, theResult.size());
    }

    @Test
    public void testOneLiteral() {
        Map<String, Object> theResult = new JSONParser().fromJSON("{ \"name\" : \"10\"}");
        assertEquals(1, theResult.size());
        assertEquals("10", theResult.get("name"));
    }

    @Test
    public void testTwoLiterals() {
        Map<String, Object> theResult = new JSONParser().fromJSON("{ \"name\": \"10\"\n\"value\" : \"22\"}");
        assertEquals(2, theResult.size());
        assertEquals("10", theResult.get("name"));
        assertEquals("22", theResult.get("value"));
    }

    @Test
    public void testNestedObject() {
        Map<String, Object> theResult = new JSONParser().fromJSON("{ \"name\": {\"key\" : \"value\", \"a\" :  \"b\"}}");
        assertEquals(1, theResult.size());
        Map<String, Object> theNessted = (Map<String, Object>) theResult.get("name");
        assertEquals(2, theNessted.size());
        assertEquals("value", theNessted.get("key"));
        assertEquals("b", theNessted.get("a"));
    }
}