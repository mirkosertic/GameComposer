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
package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.teavm.firebase.Firebase;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseChildAdded;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseDataSnapshot;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseRef;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseRemoteEvent;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseRemoteMessage;
import de.mirkosertic.gameengine.type.UUID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.teavm.jso.JSBody;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;
import org.teavm.jso.core.JSString;

public class TeaVMFirebaseNetworkConnector extends DefaultNetworkConnector {

    public static class FrameCounter {

        private int ttl;
        private final FirebaseRef ref;

        public FrameCounter(int aTTL, FirebaseRef aRef) {
            ttl = aTTL;
            ref = aRef;
        }

        public boolean decreaseTTLAndTestIfAlive() {
            return ttl-- > 0;
        }

        public void collectGarbage() {
            ref.remove();
        }
    }

    public abstract static class InstanceID implements JSObject {

        @JSProperty("instanceId")
        public abstract String getInstanceId();

        @JSProperty("instanceId")
        public abstract void setInstanceId(String aInstanceID);

        @JSBody(
                params = {},
                script = "return new Object();"
        )
        public native static InstanceID create();
    }

    private final String instanceID;
    private final FirebaseRef eventsRef;
    private final List<Map<String, Object>> receivedEvents;
    private final List<FrameCounter> garbageCollectionList;

    public TeaVMFirebaseNetworkConnector(String aFirebaseURL, String aUniqueConnectionID, boolean aTruncateDB) {

        instanceID = UUID.randomUID();

        receivedEvents = new ArrayList<>();
        garbageCollectionList = new ArrayList<>();

        FirebaseRef theBaseRef = Firebase.create(aFirebaseURL);
        if (aTruncateDB) {
            theBaseRef.remove();
        }

        theBaseRef = theBaseRef.child(aUniqueConnectionID);

        FirebaseRef theInstances = theBaseRef.child("instances");
        InstanceID theInstanceID = InstanceID.create();
        theInstanceID.setInstanceId(instanceID);
        theInstances.push(theInstanceID);

        eventsRef = theBaseRef.child("events");
        eventsRef.on("child_added", new FirebaseChildAdded() {
            @Override
            public void handleChildAdded(FirebaseDataSnapshot aChildSnapshot, JSObject aPrevChildName) {
                FirebaseRemoteEvent theEvent = aChildSnapshot.val();
                String theInstanceID = theEvent.getEPID();
                if (!instanceID.equals(theInstanceID)) {
                    String theEventTS = theEvent.getEventTimestamp();

                    // New event from another instance
                    TeaVMLogger.info("New message : " + aChildSnapshot.key()+" @ " + theEventTS);

                    for (FirebaseRemoteMessage theMessage : theEvent.getEvents()) {
                        receivedEvents.add(convert(theMessage));
                    }
                }
            }
        });
    }

    private Map<String, Object> convert(FirebaseRemoteMessage aMessage) {
        HashMap<String, Object> theResult = new HashMap<>();
        for (String theKey : aMessage.getKeys()) {
            JSObject theValue = aMessage.get(theKey);
            if (JSString.isInstance(theValue)) {
                theResult.put(theKey, ((JSString) theValue).stringValue());
            } else {
                theResult.put(theKey, convert((FirebaseRemoteMessage) theValue));
            }
        }
        return theResult;
    }

    private FirebaseRemoteMessage convert(Map<String, Object> aEvent) {
        FirebaseRemoteMessage theMessage = FirebaseRemoteMessage.create();

        Set<String> theKeys = new HashSet<>();

        for (Map.Entry<String, Object> theEnty : aEvent.entrySet()) {
            Object theValue = theEnty.getValue();
            if (theValue instanceof String) {
                theMessage.put(theEnty.getKey(), JSString.valueOf((String) theValue));
                theKeys.add(theEnty.getKey());
            } else if (theValue instanceof Map) {
                theKeys.add(theEnty.getKey());
                theMessage.put(theEnty.getKey(), convert((Map<String, Object>) theEnty.getValue()));
            }
        }

        theMessage.setKeys(theKeys.toArray(new String[theKeys.size()]));

        return theMessage;
    }

    @Override
    public int send(List<Map<String, Object>> aEventsToSend) {
        if (!aEventsToSend.isEmpty()) {

            List<FirebaseRemoteMessage> theEvents = new ArrayList<>();
            for (Map<String, Object> theEvent : aEventsToSend) {
                theEvents.add(convert(theEvent));
            }

            FirebaseRemoteEvent theEvent = FirebaseRemoteEvent.create();
            theEvent.setEPID(instanceID);
            theEvent.setEventTimestamp("" + System.currentTimeMillis());
            theEvent.setPayloadSize(aEventsToSend.size());
            theEvent.setEvents(theEvents.toArray(new FirebaseRemoteMessage[aEventsToSend.size()]));

            FirebaseRef theReference = eventsRef.push(theEvent);

            // Event will be garbage collected after 10 seonds
            // We are running at 60 Frames / second
            garbageCollectionList.add(new FrameCounter(60 * 10, theReference));
        }
        return aEventsToSend.size();
    }

    @Override
    public List<Map<String, Object>> receive() {
        // First of all, we do some garbage collection
        List<FrameCounter> theSurvived = new ArrayList<>();
        for (FrameCounter theCounter : garbageCollectionList) {
            if (theCounter.decreaseTTLAndTestIfAlive()) {
                theSurvived.add(theCounter);
            } else {
                // Reference should be garbage collected
                theCounter.collectGarbage();
            }
        }
        garbageCollectionList.clear();
        garbageCollectionList.addAll(theSurvived);

        try {
            if (receivedEvents.isEmpty()) {
                return EMPTY;
            }
            return new ArrayList<>(receivedEvents);
        } finally {
            receivedEvents.clear();
        }
    }
}