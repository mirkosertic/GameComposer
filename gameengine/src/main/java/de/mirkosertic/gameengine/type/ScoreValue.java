/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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