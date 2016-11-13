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

import de.mirkosertic.gameengine.core.GameRuntime;
import de.mirkosertic.gameengine.core.Promise;
import de.mirkosertic.gameengine.sound.GameSoundSystem;
import de.mirkosertic.gameengine.teavm.howler.Config;
import de.mirkosertic.gameengine.teavm.howler.Howl;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

class TeaVMGameSoundSystem implements GameSoundSystem<TeaVMSound> {

    private final GameRuntime runtime;
    private final Map<String, Howl> howls;

    public TeaVMGameSoundSystem(GameRuntime aRuntime) {
        runtime = aRuntime;
        howls = new HashMap<>();
    }

    private String guessFormat(ResourceName aResourceName) {
        String theName = aResourceName.name;
        int p = theName.lastIndexOf(".");
        return theName.substring(p+1);
    }

    @Override
    public Promise<TeaVMSound, String> play(ResourceName aResourceName) {
        return new Promise<>((Promise.Executor) (aResolver, aRejector) -> runtime.getResourceCache().getResourceFor(aResourceName).thenContinue(aResult -> {
            TeaVMSoundResource theResource = (TeaVMSoundResource) aResult;

            Howl theHowl = howls.get(theResource.getName());
            if (theHowl == null) {
                Config theConfig = Config.createConfig();
                theConfig.setSrc(new String[] {theResource.getName()});
                theConfig.setFormat(new String[] {guessFormat(aResourceName)});
                theHowl = Howl.createHowl(theConfig);
                howls.put(theResource.getName(), theHowl);
            }

            aResolver.resolve(new TeaVMSound(theHowl, theHowl.play()));
        }));
    }

    @Override
    public void stop(TeaVMSound aSoundObject) {
        aSoundObject.howl.stop(aSoundObject.soundID);
    }
}