package de.mirkosertic.gameengine.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Animation {

    private final String uuid;
    private final String name;
    private final List<ResourceName> animationSequence;

    public Animation(String aName) {
        this(UUID.randomUID(), aName, new ArrayList<ResourceName>());
    }

    private Animation(String aUUID, String aName, List<ResourceName> aAnimationSequence) {
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
        return new Animation(uuid, aName, new ArrayList<>(animationSequence));
    }

    public Animation addToAnimationSequence(ResourceName aName) {
        List<ResourceName> theNewSequence = new ArrayList<>(animationSequence);
        theNewSequence.add(aName);
        return new Animation(uuid, name, theNewSequence);
    }

    public Animation removeFromAnimationSequence(ResourceName aName) {
        List<ResourceName> theNewSequence = new ArrayList<>(animationSequence);
        theNewSequence.remove(aName);
        return new Animation(uuid, name, theNewSequence);
    }

    public Animation removeFromAnimationSequence(int aIndex) {
        List<ResourceName> theNewSequence = new ArrayList<>(animationSequence);
        theNewSequence.remove(aIndex);
        return new Animation(uuid, name, theNewSequence);
    }

    public int getSequenceSize() {
        return animationSequence.size();
    }

    public ResourceName getResourceByIndex(int aIndex) {
        return animationSequence.get(aIndex);
    }

    public ResourceName computeCurrentFrame(long aGameTime, Integer aSpeed) {
        if (animationSequence.isEmpty()) {
            return null;
        }
        int theFrame = (int)((aGameTime / 1000d * aSpeed) % animationSequence.size());
        return animationSequence.get(theFrame);
    }

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
        return new Animation(theUUID, theName, theAnimationSequence);
    }
}