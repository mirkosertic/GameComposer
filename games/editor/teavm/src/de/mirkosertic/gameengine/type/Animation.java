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