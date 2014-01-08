package de.mirkosertic.gameengine.event;

import de.mirkosertic.gameengine.type.Angle;
import de.mirkosertic.gameengine.type.ScoreValue;
import de.mirkosertic.gameengine.type.TextExpression;

final class TypeConverter {

    private TypeConverter() {
    }

    static Object convertTo(Object aValue, Class aTargetType) {
        if (aValue instanceof String) {
            if (aTargetType == TextExpression.class) {
                return new TextExpression((String) aValue);
            }
            if (aTargetType == Float.class) {
                return Float.valueOf((String) aValue);
            }
            if (aTargetType == Integer.class) {
                return Integer.valueOf((String) aValue);
            }
            if (aTargetType == Long.class) {
                return Long.valueOf((String) aValue);
            }
            if (aTargetType == Double.class) {
                return Double.valueOf((String) aValue);
            }
            if (aTargetType == Boolean.class) {
                return Boolean.valueOf((String) aValue);
            }
            if (aTargetType == Angle.class) {
                return new Angle(Integer.valueOf((String) aValue));
            }
            if (aTargetType == ScoreValue.class) {
                return new ScoreValue(Long.valueOf((String) aValue));
            }
        }
        return aValue;
    }
}
