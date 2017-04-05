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

import org.teavm.interop.Export;

import java.util.ArrayList;
import java.util.List;

public class WASMStringPool {

    private final static List<StringBuffer> BUFFERS = new ArrayList<>();

    @Export(name = "newStringPoolID")
    public static int newStringPoolIO() {
        BUFFERS.add(new StringBuffer());
        return BUFFERS.size() - 1;
    }

    @Export(name = "addCharToStringPool")
    public static void addCharToStringPool(int aPoolID, int aChar) {
        BUFFERS.get(aPoolID).append((char) aChar);
    }

    public static String getStringPool(int aPoolID) {
        return BUFFERS.get(aPoolID).toString();
    }
}
