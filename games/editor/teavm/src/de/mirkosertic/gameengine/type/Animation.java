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
package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.ArrayUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation implements Distributable {

    private final String uuid;
    private final String name;
    private final ResourceName[] animationSequence;

    public Animation(String aName) {
        this(UUID.randomUID(), aName, new ResourceName[0]);
    }

    private Animation(String aUUID, String aName, ResourceName[] aAnimationSequence) {
        name = aName;
        uuid = aUUID;
        animationSequence = aAnimationSequence;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public Animation changeName(String aName) {
        return new Animation(uuid, aName, animationSequence);
    }

    public Animation addToAnimationSequence(ResourceName aName) {
        List<ResourceName> theNewSequence = ArrayUtils.asList(animationSequence);
        theNewSequence.add(aName);
        return new Animation(uuid, name, theNewSequence.toArray(new ResourceName[theNewSequence.size()]));
    }

    public Animation removeFromAnimationSequence(ResourceName aName) {
        List<ResourceName> theNewSequence = ArrayUtils.asList(animationSequence);
        theNewSequence.remove(aName);
        return new Animation(uuid, name, theNewSequence.toArray(new ResourceName[theNewSequence.size()]));
    }

    public Animation removeFromAnimationSequence(int aIndex) {
        List<ResourceName> theNewSequence = ArrayUtils.asList(animationSequence);
        theNewSequence.remove(aIndex);
        return new Animation(uuid, name, theNewSequence.toArray(new ResourceName[theNewSequence.size()]));
    }

    public int getSequenceSize() {
        return animationSequence.length;
    }

    public ResourceName getResourceByIndex(int aIndex) {
        return animationSequence[aIndex];
    }

    public ResourceName computeCurrentFrame(long aGameTime, Integer aSpeed) {
        if (animationSequence.length == 0) {
            return null;
        }
        int theFrame = (int)((aGameTime / 1000d * aSpeed) % animationSequence.length);
        return animationSequence[theFrame];
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("uuid", uuid);
        theResult.put("name", name);
        List<Map<String, Object>> theResources = new ArrayList<>();
        for (ResourceName theName : animationSequence) {
            theResources.add(theName.serialize());
        }
        theResult.put("animationSequence", theResources);
        return theResult;
    }

    public static Animation deserialize(Map<String, Object> aData) {
        String theName = (String) aData.get("name");
        String theUUID = (String) aData.get("uuid");
        List<ResourceName> theAnimationSequence = new ArrayList<>();
        for (Map<String, Object> theEntry : (List<Map<String, Object>>) aData.get("animationSequence")) {
            theAnimationSequence.add(ResourceName.deserialize(theEntry));
        }
        return new Animation(theUUID, theName, theAnimationSequence.toArray(new ResourceName[theAnimationSequence.size()]));
    }
}