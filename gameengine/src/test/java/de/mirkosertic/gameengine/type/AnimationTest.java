package de.mirkosertic.gameengine.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;

public class AnimationTest {

    @Test
    public void testInit() {
        Animation theAnimation = new Animation("Test");
        assertEquals("Test", theAnimation.getName());
        Long.valueOf(theAnimation.getUuid());
        assertEquals(0, theAnimation.getSequenceSize());
    }

    @Test
    public void testChangeName() throws Exception {
        Animation theAnimation = new Animation("Test");
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("Res"));
        assertEquals(1, theAnimation.getSequenceSize());
        Animation theNewAnimation = theAnimation.changeName("Lala");
        assertEquals("Lala", theNewAnimation.getName());
        assertNotSame(theAnimation, theNewAnimation);
        assertEquals(1, theNewAnimation.getSequenceSize());
        assertEquals(new ResourceName("Res"), theNewAnimation.getResourceByIndex(0));
    }

    @Test
    public void testAddToAnimationSequence() throws Exception {
        Animation theAnimation = new Animation("Test");
        Animation theNewAnimation = theAnimation.addToAnimationSequence(new ResourceName("Res"));
        assertEquals(1, theNewAnimation.getSequenceSize());
        assertEquals(0, theAnimation.getSequenceSize());
        assertNotSame(theAnimation, theNewAnimation);
    }

    @Test
    public void testRemoveFromAnimationSequence() throws Exception {
        Animation theAnimation = new Animation("Test");
        Animation theNewAnimation = theAnimation.addToAnimationSequence(new ResourceName("Res"));
        Animation theDeletedAnimation = theNewAnimation.removeFromAnimationSequence(new ResourceName("Res"));
        assertEquals(1, theNewAnimation.getSequenceSize());
        assertEquals(0, theAnimation.getSequenceSize());
        assertEquals(0, theDeletedAnimation.getSequenceSize());
        assertNotSame(theAnimation, theNewAnimation);
        assertNotSame(theAnimation, theDeletedAnimation);
        assertNotSame(theNewAnimation, theDeletedAnimation);
    }

    @Test
    public void testRemoveFromAnimationSequenceWithIndex() throws Exception {
        Animation theAnimation = new Animation("Test");
        Animation theNewAnimation = theAnimation.addToAnimationSequence(new ResourceName("Res"));
        Animation theDeletedAnimation = theNewAnimation.removeFromAnimationSequence(0);
        assertEquals(1, theNewAnimation.getSequenceSize());
        assertEquals(0, theAnimation.getSequenceSize());
        assertEquals(0, theDeletedAnimation.getSequenceSize());
        assertNotSame(theAnimation, theNewAnimation);
        assertNotSame(theAnimation, theDeletedAnimation);
        assertNotSame(theNewAnimation, theDeletedAnimation);
    }

    @Test
    public void testSerialize() throws Exception {
        Animation theAnimation = new Animation("Test").addToAnimationSequence(new ResourceName("1")).addToAnimationSequence(new ResourceName("2"));
        Map<String, Object> theData = theAnimation.serialize();
        assertEquals(3, theData.size());
        assertEquals("Test", theData.get("name"));
        assertEquals(theAnimation.getUuid(), theData.get("uuid"));
        List<Map<String, Object>> theAnimationSequence = (List<Map<String, Object>>) theData.get("animationSequence");
        assertEquals(2, theAnimationSequence.size());
        Map<String, Object> theEntry1 = theAnimationSequence.get(0);
        assertEquals("1", theEntry1.get("name"));
        Map<String, Object> theEntry2 = theAnimationSequence.get(1);
        assertEquals("2", theEntry2.get("name"));
    }

    @Test
    public void testDeserialize() throws Exception {
        Map<String, Object> theData = new HashMap<>();
        theData.put("uuid", "id");
        theData.put("name", "Test");
        List<Map<String, Object>> theAnimationSequence = new ArrayList<>();
        theAnimationSequence.add(new ResourceName("1").serialize());
        theAnimationSequence.add(new ResourceName("2").serialize());
        theData.put("animationSequence", theAnimationSequence);

        Animation theAnimation = Animation.deserialize(theData);
        assertEquals("id", theAnimation.getUuid());
        assertEquals("Test", theAnimation.getName());
        assertEquals(2, theAnimation.getSequenceSize());
        assertEquals(new ResourceName("1"), theAnimation.getResourceByIndex(0));
        assertEquals(new ResourceName("2"), theAnimation.getResourceByIndex(1));
    }

    @Test
    public void testComputeCurrentFrame() throws Exception {
        Animation theAnimation = new Animation("Test");
        assertNull(theAnimation.computeCurrentFrame(0, 0));
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("1"));
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("2"));
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("3"));
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("4"));
        theAnimation = theAnimation.addToAnimationSequence(new ResourceName("5"));

        assertEquals(new ResourceName("1"), theAnimation.computeCurrentFrame(0, 5));
        assertEquals(new ResourceName("2"), theAnimation.computeCurrentFrame(200, 5));
        assertEquals(new ResourceName("2"), theAnimation.computeCurrentFrame(399, 5));
        assertEquals(new ResourceName("3"), theAnimation.computeCurrentFrame(400, 5));
        assertEquals(new ResourceName("3"), theAnimation.computeCurrentFrame(599, 5));
        assertEquals(new ResourceName("4"), theAnimation.computeCurrentFrame(600, 5));
        assertEquals(new ResourceName("4"), theAnimation.computeCurrentFrame(601, 5));
        assertEquals(new ResourceName("5"), theAnimation.computeCurrentFrame(800, 5));
        assertEquals(new ResourceName("1"), theAnimation.computeCurrentFrame(1000, 5));
        assertEquals(new ResourceName("2"), theAnimation.computeCurrentFrame(1200, 5));
        assertEquals(new ResourceName("3"), theAnimation.computeCurrentFrame(1400, 5));
        assertEquals(new ResourceName("4"), theAnimation.computeCurrentFrame(1600, 5));
        assertEquals(new ResourceName("5"), theAnimation.computeCurrentFrame(1800, 5));
    }
}