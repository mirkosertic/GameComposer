package de.mirkosertic.gameengine.type;

import de.mirkosertic.gameengine.annotations.ReflectiveMethod;

import java.util.HashMap;
import java.util.Map;

public class ScoreValue implements Reflectable, Distributable {

    private static final ScoreValueClassInformation CIINSTANCE = new ScoreValueClassInformation();

    public final long score;

    public ScoreValue() {
        this(0);
    }

    public ScoreValue(long aScore) {
        score = aScore;
    }

    @ReflectiveMethod
    public ScoreValue incrementBy(long aAmount) {
        return new ScoreValue(score + aAmount);
    }

    @ReflectiveMethod
    public ScoreValue resetToZero() {
        return new ScoreValue(0);
    }

    public Long get() {
        return score;
    }

    @Override
    public ScoreValueClassInformation getClassInformation() {
        return CIINSTANCE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreValue that = (ScoreValue) o;

        return score == that.score;

    }

    @Override
    public int hashCode() {
        return (int) (score ^ (score >>> 32));
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("score", Long.toString(score));
        return theResult;
    }

    public static ScoreValue deserialize(Map<String, Object> aSerializesData) {
        long theScore = Long.parseLong((String) aSerializesData.get("score"));
        return new ScoreValue(theScore);
    }
}