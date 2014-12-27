package de.mirkosertic.gameengine.sprite;

import de.mirkosertic.gameengine.event.Property;
import de.mirkosertic.gameengine.type.Field;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SpriteClassInformationTest {

    @Test
    public void testCurrentAnimation() {
        assertEquals("currentAnimation", SpriteClassInformation.INSTANCE.getFieldByName("currentAnimation").getName());
        assertEquals("speed", SpriteClassInformation.INSTANCE.getFieldByName("speed").getName());
        assertEquals(2, SpriteClassInformation.INSTANCE.getFields().length);

        Property theProperty = mock(Property.class);
        Sprite theSprite = mock(Sprite.class);
        when(theSprite.currentAnimationProperty()).thenReturn(theProperty);

        Field theField = SpriteClassInformation.INSTANCE.getFieldByName("currentAnimation");
        assertSame(SpriteClassInformation.CURRENTANIMATION, theField);
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theSprite));
    }

    @Test
    public void testSpeed() {
        assertEquals("currentAnimation", SpriteClassInformation.INSTANCE.getFieldByName("currentAnimation").getName());
        assertEquals("speed", SpriteClassInformation.INSTANCE.getFieldByName("speed").getName());
        assertEquals(2, SpriteClassInformation.INSTANCE.getFields().length);

        Property theProperty = mock(Property.class);
        Sprite theSprite = mock(Sprite.class);
        when(theSprite.speedProperty()).thenReturn(theProperty);

        Field theField = SpriteClassInformation.INSTANCE.getFieldByName("speed");
        assertSame(SpriteClassInformation.SPEED, theField);
        assertEquals(Property.class, theField.getType());
        assertSame(theProperty, theField.getValue(theSprite));
    }
}