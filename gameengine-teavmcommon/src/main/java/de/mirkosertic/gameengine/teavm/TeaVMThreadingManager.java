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
package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.core.FutureResult;
import de.mirkosertic.gameengine.core.Job;
import de.mirkosertic.gameengine.core.ThreadingManager;

public class TeaVMThreadingManager implements ThreadingManager {

    @Override
    public FutureResult submit(final Job aJob) {

        final Object LOCK = new Object();

        synchronized (LOCK) {
            final Thread theThread = new Thread() {
                @Override
                public void run() {
                    synchronized (LOCK) {
                        TeaVMLogger.time("Thread");
                        aJob.run();
                        TeaVMLogger.timeEnd("Thread");
                    }
                }
            };
            theThread.start();
        }

        return new FutureResult() {
            @Override
            public void waitForCompletion() throws Exception {
                synchronized (LOCK) {
                    return;
                }

            }
        };
    }
}