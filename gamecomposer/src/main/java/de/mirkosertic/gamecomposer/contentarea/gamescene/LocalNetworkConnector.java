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
package de.mirkosertic.gamecomposer.contentarea.gamescene;

import de.mirkosertic.gameengine.network.DefaultNetworkConnector;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class LocalNetworkConnector extends DefaultNetworkConnector {

    private static class LocalEvent {
        private final LocalNetworkConnector connector;
        private final byte[] data;

        public LocalEvent(LocalNetworkConnector aConnector, byte[] aData) {
            connector = aConnector;
            data = aData;
        }
    }

    private static final List<LocalNetworkConnector> KNOWN_CONNECTORS = Collections.synchronizedList(new ArrayList<>());

    private Stack<LocalEvent> receivedEvents;

    public LocalNetworkConnector() {
        receivedEvents = new Stack<>();
        KNOWN_CONNECTORS.add(this);
    }

    @Override
    public int send(List<Map<String, Object>> aEventsToSend) {
        try {
            ByteArrayOutputStream theData = new ByteArrayOutputStream();
            ObjectOutputStream theOS = new ObjectOutputStream(theData);
            theOS.writeObject(aEventsToSend);
            theOS.flush();

            byte[] theRawData = theData.toByteArray();

            LocalEvent theEvent = new LocalEvent(this, theRawData);
            for (LocalNetworkConnector theConnector : KNOWN_CONNECTORS) {
                theConnector.handle(theEvent);
            }

            return theRawData.length;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }

    public void shutdown() {
        KNOWN_CONNECTORS.remove(this);
    }

    private void handle(LocalEvent aEvent) {
        if (aEvent.connector != this) {
            receivedEvents.add(aEvent);
        }
    }

    @Override
    public List<Map<String, Object>> receive() {
        if (!receivedEvents.isEmpty()) {
            LocalEvent theEvent = receivedEvents.pop();
            try {
                ObjectInputStream theStream = new ObjectInputStream(new ByteArrayInputStream(theEvent.data));
                return (List<Map<String, Object>>) theStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return EMPTY;
    }
}