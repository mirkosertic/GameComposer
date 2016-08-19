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
package de.mirkosertic.gameengine.android;

import android.content.res.AssetFileDescriptor;
import de.mirkosertic.gameengine.core.GameResource;
import de.mirkosertic.gameengine.core.GameResourceType;
import de.mirkosertic.gameengine.type.ResourceName;

public class AndroidSoundResource implements GameResource {

    public final AssetFileDescriptor fileDescriptor;
    public final ResourceName resourceName;

    AndroidSoundResource(ResourceName aResourceName, AssetFileDescriptor aFileDescriptor) {
        fileDescriptor = aFileDescriptor;
        resourceName = aResourceName;
    }

    @Override
    public GameResourceType getType() {
        return GameResourceType.SOUND;
    }
}
