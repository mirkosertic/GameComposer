package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.ScoreValue;
import de.mirkosertic.gameengine.type.TextExpression;
import org.junit.Assert;
import org.junit.Test;

public class TypeConverterTest {

    @Test
    public void testConversion() {
        Assert.assertEquals(TypeConverter.convertTo(Class.class, Class.class), Class.class);
        Assert.assertEquals(TypeConverter.convertTo("1", String.class), "1");
        Assert.assertEquals(TypeConverter.convertTo("1", Integer.class), 1);
        Assert.assertEquals(TypeConverter.convertTo("1", Long.class), 1l);
        Assert.assertEquals(TypeConverter.convertTo("1", Double.class), 1d);
        Assert.assertEquals(TypeConverter.convertTo("1", Long.class), 1l);
        Assert.assertEquals(TypeConverter.convertTo("1", Float.class), 1f);
        Assert.assertEquals(TypeConverter.convertTo("1", Angle.class), new Angle(1));
        Assert.assertEquals(TypeConverter.convertTo("1", TextExpression.class), new TextExpression("1"));
        Assert.assertEquals(TypeConverter.convertTo("true", Boolean.class), true);
        Assert.assertEquals(TypeConverter.convertTo("1", ScoreValue.class), new ScoreValue(1));
    }
}
