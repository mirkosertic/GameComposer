package de.mirkosertic.gameengine.gwt;

import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JSONUtils {

    private JSONUtils() {
    }

    public static Map<String, Object> toMap(JSONValue aValue) {
        HashMap<String, Object> theResult = new HashMap<String, Object>();
        JSONObject theObject = aValue.isObject();
        for (String theKey : theObject.keySet()) {
            JSONValue theValue = theObject.get(theKey);
            JSONArray theJSonArray = theValue.isArray();
            if (theJSonArray != null) {
                List<Object> theSingle = new ArrayList<Object>();
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