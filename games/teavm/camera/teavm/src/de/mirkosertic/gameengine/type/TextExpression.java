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

import java.util.HashMap;
import java.util.Map;

public class TextExpression implements Distributable {

    public final String expression;

    public TextExpression(String aExpression) {
        expression = aExpression;
    }

    public String get() {
        return expression;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextExpression that = (TextExpression) o;

        return expression.equals(that.expression);

    }

    @Override
    public int hashCode() {
        return expression.hashCode();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> theResult = new HashMap<>();
        theResult.put("expression", expression);
        return theResult;
    }

    public static TextExpression deserialize(Map<String,Object> aSerializedData) {
        String theExpression = (String) aSerializedData.get("expression");
        return new TextExpression(theExpression);
    }
}
