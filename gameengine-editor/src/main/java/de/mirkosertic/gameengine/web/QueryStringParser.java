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
package de.mirkosertic.gameengine.web;

import java.util.HashMap;
import java.util.Map;

public class QueryStringParser {

    private final Map<String, String> params;

    public QueryStringParser(String aParamString) {
        params = new HashMap<>();

        while (aParamString.length() > 0) {
            int p2 = aParamString.indexOf("&");
            String theParam;
            if (p2 > 0) {
                theParam = aParamString.substring(0, p2);
                aParamString = aParamString.substring(p2 + 1);
            } else {
                theParam = aParamString;
                aParamString = "";
            }

            int p3 = theParam.indexOf("=");
            if (p3>= 0) {
                String theKey = theParam.substring(0, p3);
                String theValue = theParam.substring(p3 + 1);
                params.put(theKey, Router.decodeURI(theValue));
            } else {
                params.put(theParam, "");
            }
        }
    }

    public boolean contains(String aKey) {
        return params.containsKey(aKey);
    }

    public String getValue(String aKey) {
        return params.get(aKey);
    }
}
