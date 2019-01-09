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

import de.mirkosertic.bytecoder.api.OpaqueReferenceType;
import de.mirkosertic.bytecoder.api.web.OpaqueReferenceArray;
import de.mirkosertic.gameengine.bytecoder.pixi.SpritesheetJSONResource;
import de.mirkosertic.gameengine.bytecoder.pixi.Texture;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class BytecoderLoadedSpriteSheet implements LoadedSpriteSheet {

    private static native OpaqueReferenceArray keysOf(OpaqueReferenceType aObject);

    public static native String toFrameID(OpaqueReferenceType aType);

    private final Map<String, BytecoderTextureResource> knownResources;

    public BytecoderLoadedSpriteSheet(SpritesheetJSONResource aJSON) {
        knownResources = new HashMap<>();
        OpaqueReferenceArray theKeys = keysOf(aJSON.getFrames());
        for (int i=0;i<theKeys.objectArrayLength();i++) {
            OpaqueReferenceType theFrameID = theKeys.get(i);
            String theFrameName = toFrameID(theFrameID);
            Texture theTexture = Texture.createFromFrame(theFrameName);
            BytecoderLogger.INSTANCE.info("Loaded frame " + theFrameName);
            BytecoderTextureResource theResource = new BytecoderTextureResource(theTexture, theFrameName);
            knownResources.put(theFrameName, theResource);
        }

        BytecoderLogger.INSTANCE.info("Loading finished with " + knownResources.size() + " frames");
    }

    @Override
    public GameResource getResourceFor(ResourceName aResourceName) {
        String theFrameName = aResourceName.getOnlyFileName();
        return knownResources.get(theFrameName);
    }
}