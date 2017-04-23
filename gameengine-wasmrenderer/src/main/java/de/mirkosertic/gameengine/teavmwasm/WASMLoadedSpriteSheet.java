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
package de.mirkosertic.gameengine.teavmwasm;

import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.LoadedSpriteSheet;
import de.mirkosertic.gameengine.type.ResourceName;

import java.util.HashMap;
import java.util.Map;

public class WASMLoadedSpriteSheet implements LoadedSpriteSheet {

    private Map<String, GameResource> knownResources;

    public WASMLoadedSpriteSheet() {
        knownResources = new HashMap<>();
    }

    @Override
    public GameResource getResourceFor(ResourceName aResourceName) {
        return knownResources.get(aResourceName.getOnlyFileName());
    }

    public void registerResource(String aResourceName, int aExternalResourceID) {
        knownResources.put(aResourceName, new WASMTextureResource(aExternalResourceID));
    }
}
