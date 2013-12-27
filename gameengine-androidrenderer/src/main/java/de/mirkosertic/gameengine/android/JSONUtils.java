package de.mirkosertic.gameengine.android;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

class JSONUtils {

    private JSONUtils() {
    }

    public static Map<String, Object> toMap(JSONObject aValue) {
        HashMap<String, Object> theResult = new HashMap<>();
        for (Iterator theKeyIterator = aValue.keys(); theKeyIterator.hasNext();) {
            String theKey = (String) theKeyIterator.next();
            if (aValue.isNull(theKey)) {
                continue;
            }
            JSONArray theArray = aValue.optJSONArray(theKey);
            if (theArray != null) {
                // Value is an array
                List<Object> theObjects = new ArrayList<>();
                for (int i=0;i<theArray.length();i++) {
                    if (!theArray.isNull(i)) {
                        JSONObject theSingleObject = theArray.optJSONObject(i);
                        if (theSingleObject != null) {
                            theObjects.add(toMap(theSingleObject));
                        } else {
                            theObjects.add(theArray.optString(i));
                        }
                    }
                }
                theResult.put(theKey, theObjects);
                continue;
            }
            JSONObject theObject = aValue.optJSONObject(theKey);
            if (theObject != null) {
                theResult.put(theKey, toMap(theObject));
                continue;
            }

            theResult.put(theKey, aValue.optString(theKey));
        }

        return theResult;
    }
}