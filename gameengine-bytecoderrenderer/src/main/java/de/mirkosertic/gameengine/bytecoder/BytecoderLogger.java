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

import de.mirkosertic.bytecoder.api.web.Console;
import de.mirkosertic.gameengine.core.Logger;

public class BytecoderLogger implements Logger {

    public static final BytecoderLogger INSTANCE = new BytecoderLogger();

    private static Console console = Console.console();

    @Override
    public void info(String aMessage) {
        console.log("INFO: " + aMessage);
    }

    @Override
    public void error(String aMessage) {
        console.log("ERROR: " + aMessage);
    }

    @Override
    public void time(String aLabel) {
        //console.profileTime(aLabel);
    }

    @Override
    public void timeEnd(String aLabel) {
        //console.profileTimeEnd(aLabel);
    }
}