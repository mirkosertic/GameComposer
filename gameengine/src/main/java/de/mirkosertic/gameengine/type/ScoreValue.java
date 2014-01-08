package de.mirkosertic.gameengine.type;

import java.util.HashMap;
import java.util.Map;

public class ScoreValue {

    public final long score;

    public ScoreValue() {
        this(0);
    }

    public ScoreValue(long aScore) {
        score = aScore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoreValue that = (ScoreValue) o;

        if (score != that.score) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (score ^ (score >>> 32));
    }

    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<String, Object>();
        theResult.put("score", Long.toString(score));
        return theResult;
    }

    public static ScoreValue deserialize(Map<String, Object> aSerializesData) {
        long theScore = Long.parseLong((String) aSerializesData.get("score"));
        return new ScoreValue(theScore);
    }
}
