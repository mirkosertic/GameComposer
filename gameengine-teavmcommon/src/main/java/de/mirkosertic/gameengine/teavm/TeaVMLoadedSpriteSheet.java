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

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.teavm.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.teavm.pixi.Texture;
import de.mirkosertic.gameengine.type.ResourceName;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;

import java.util.HashMap;
import java.util.Map;

public class TeaVMLoadedSpriteSheet implements LoadedSpriteSheet {

    @JSBody(params = {"aObject"}, script = "return Object.keys(aObject);")
    private static native String[] keysOf(JSObject aObject);

    private final Map<String, TeaVMTextureResource> knownResources;

    public TeaVMLoadedSpriteSheet(SpritesheetJSONResource aJSON) {
        knownResources = new HashMap<>();
        for (String theFrameID : keysOf(aJSON.getFrames())) {
            Texture theTexture = Texture.createFromFrame(theFrameID);
            TeaVMTextureResource theResource = new TeaVMTextureResource(theTexture, theFrameID);
            knownResources.put(theFrameID, theResource);
        }

        TeaVMLogger.info("Loading finished with " + knownResources.size() + " frames");
    }

    @Override
    public GameResource getResourceFor(ResourceName aResourceName) {
        String theFrameName = aResourceName.getOnlyFileName();
        return knownResources.get(theFrameName);
    }
}