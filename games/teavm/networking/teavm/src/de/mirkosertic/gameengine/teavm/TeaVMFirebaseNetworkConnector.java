package de.mirkosertic.gameengine.teavm;

import de.mirkosertic.gameengine.network.DefaultNetworkConnector;
import de.mirkosertic.gameengine.teavm.firebase.Firebase;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseChildAdded;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseDataSnapshot;
import de.mirkosertic.gameengine.teavm.firebase.FirebaseRef;
import de.mirkosertic.gameengine.teavm.json.JSONMap;
import de.mirkosertic.gameengine.type.UUID;

import org.teavm.dom.browser.Window;
import org.teavm.jso.JS;
import org.teavm.jso.JSObject;
import org.teavm.jso.JSProperty;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TeaVMFirebaseNetworkConnector extends DefaultNetworkConnector {

    private static final JSObject EVENT_PRODUCER_ID = JS.wrap("epid");
    private static final JSObject EVENT_TS_ID = JS.wrap("evts");
    private static final JSObject EVENT_PAYLOAD_SIZE = JS.wrap("payloadsize");

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

    public static interface InstanceID extends JSObject {

        @JSProperty("instanceId")
        String getInstanceId();

        @JSProperty("instanceId")
        void setInstanceId(String aInstanceID);
    }

    private final String instanceID;
    private final Window window;
    private final FirebaseRef eventsRef;
    private final List<Map<String, Object>> receivedEvents;
    private final List<FrameCounter> garbageCollectionList;

    public TeaVMFirebaseNetworkConnector(String aFirebaseURL, String aUniqueConnectionID, TeaVMWindow aWindow, boolean aTruncateDB) {

        window = aWindow;
        instanceID = UUID.randomUID();

        receivedEvents = new ArrayList<>();
        garbageCollectionList = new ArrayList<>();

        FirebaseRef theBaseRef = ((Firebase) JS.getGlobal()).create(aFirebaseURL);
        if (aTruncateDB) {
            theBaseRef.remove();
        }

        theBaseRef = theBaseRef.child(aUniqueConnectionID);

        FirebaseRef theInstances = theBaseRef.child("instances");
        InstanceID theInstanceID = (InstanceID) window.newObject();
        theInstanceID.setInstanceId(instanceID);
        theInstances.push(theInstanceID);

        eventsRef = theBaseRef.child("events");
        eventsRef.on("child_added", new FirebaseChildAdded() {
            @Override
            public void handleChildAdded(FirebaseDataSnapshot aChildSnapshot, JSObject aPrevChildName) {
                JSObject theEvent = aChildSnapshot.val();
                String theInstanceID = JS.unwrapString(JS.get(theEvent, EVENT_PRODUCER_ID));
                if (!instanceID.equals(theInstanceID)) {
                    String theEventTS = JS.unwrapString(JS.get(theEvent, EVENT_TS_ID));

                    // New event from another instance
                    TeaVMLogger.info("New message : " + aChildSnapshot.key()+" @ " + theEventTS);

                    int thePayloadSize = Integer.parseInt(JS.unwrapString(JS.get(theEvent, EVENT_PAYLOAD_SIZE)));
                    for (int i=0;i<thePayloadSize;i++) {
                        JSObject theSingleEvent = JS.get(theEvent, JS.wrap("" + i));
                        receivedEvents.add(new JSONMap(theSingleEvent));
                    }
                }
            }
        });
    }

    private JSObject convert(Map<String, Object> aMap) {
        JSObject theMap = window.newObject();
        for (String theKey: aMap.keySet()) {
            JSObject theWrappedKey = JS.wrap(theKey);
            Object theObject = aMap.get(theKey);
            if (theObject instanceof Map) {
                JS.set(theMap, theWrappedKey, convert((Map<String, Object>) theObject));
            } else if (theObject instanceof String) {
                JS.set(theMap, theWrappedKey, JS.wrap((String) theObject));
            }
        }
        return theMap;
    }

    @Override
    public int send(List<Map<String, Object>> aEventsToSend) {
        List<JSObject>  theJSEvents = new ArrayList<>();
        for (Map<String, Object> aEvent : aEventsToSend) {
            theJSEvents.add(convert(aEvent));
        }
        if (!theJSEvents.isEmpty()) {

            JSObject theObject = window.newObject();
            JS.set(theObject, EVENT_PRODUCER_ID, JS.wrap(instanceID));
            JS.set(theObject, EVENT_TS_ID, JS.wrap("" + System.currentTimeMillis()));
            JS.set(theObject, EVENT_PAYLOAD_SIZE, JS.wrap("" + theJSEvents.size()));
            for (int i=0;i<theJSEvents.size();i++) {
                JS.set(theObject, JS.wrap("" + i), theJSEvents.get(i));
            }

            FirebaseRef theReference = eventsRef.push(theObject);
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