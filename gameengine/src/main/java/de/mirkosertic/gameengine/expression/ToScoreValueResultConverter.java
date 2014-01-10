package de.mirkosertic.gameengine.expression;

import de.mirkosertic.gameengine.type.ScoreValue;

class ToScoreValueResultConverter implements AutomaticResultConverter<Object, ScoreValue> {

    @Override
    public ScoreValue convert(Object aValue) {
        if (aValue == null) {
            return null;
        }
        if (aValue instanceof ScoreValue) {
            return (ScoreValue) aValue;
        }
        if (aValue instanceof Number) {
            return new ScoreValue(((Number) aValue).intValue());
        }
        if (aValue instanceof String) {
            return new ScoreValue(Integer.valueOf((String) aValue));
        }
        throw new IllegalArgumentException("Cannot convert " + aValue);
    }
}
