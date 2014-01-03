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
