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
package de.mirkosertic.gameengine.gwt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

class JSONUtils {

    private JSONUtils() {
    }

    public static Map<String, Object> toMap(JSONValue aValue) {
        HashMap<String, Object> theResult = new HashMap<>();
        JSONObject theObject = aValue.isObject();
        for (String theKey : theObject.keySet()) {
            JSONValue theValue = theObject.get(theKey);
            JSONArray theJSonArray = theValue.isArray();
            if (theJSonArray != null) {
                List<Object> theSingle = new ArrayList<>();
                for (int i = 0; i < theJSonArray.size(); i++) {
                    JSONValue theSingleValue = theJSonArray.get(i);
                    JSONObject theSingleObject = theSingleValue.isObject();
                    if (theSingleObject != null) {
                        theSingle.add(toMap(theSingleValue));
                    } else {
                        if (theSingleValue.isNull() == null) {
                            theSingle.add(theSingleValue.isString().stringValue());
                        }
                    }
                }
                theResult.put(theKey, theSingle);
            } else {
                if (theValue.isObject() != null) {
                    theResult.put(theKey, toMap(theValue));
                } else {
                    if (theValue.isNull() == null) {
                        theResult.put(theKey, theValue.isString().stringValue());
                    }
                }
            }
        }
        return theResult;
    }
}