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
package de.mirkosertic.gameengine.sound;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveField;
import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventType;
import de.mirkosertic.gameengine.type.ResourceName;

@InheritedClassInformation
class PlaySound extends GameEvent {

    public static final GameEventType TYPE = new GameEventType("PlaySound");

    private static final PlaySoundClassInformation CIINSTANCE = new PlaySoundClassInformation();

    @ReflectiveField
    public final ResourceName resourceName;

    public PlaySound(ResourceName aResourceName) {
        super(TYPE);
        resourceName = aResourceName;
    }

    @Override
    public PlaySoundClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}