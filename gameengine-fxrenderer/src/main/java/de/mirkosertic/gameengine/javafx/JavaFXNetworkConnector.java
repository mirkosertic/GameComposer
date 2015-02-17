package de.mirkosertic.gameengine.javafx;

import de.mirkosertic.gameengine.network.DefaultNetworkConnector;

import java.io.*;

import java.util.List;
import java.util.Map;

import java.util.concurrent.ArrayBlockingQueue;

public class JavaFXNetworkConnector extends DefaultNetworkConnector {
    
    // We simply use a static QUEUE for inter process communication
    // Replace with In-Memory Publish-Subscribe Messaging with PULL support
    // For instance with CDI Messaging...
    private static final ArrayBlockingQueue<byte[]> QUEUE = new ArrayBlockingQueue<>(10);

    public JavaFXNetworkConnector() {
    }

    @Override
    public void send(List<Map<String, Object>> aEventsToSend) {
        try {
            ByteArrayOutputStream theData = new ByteArrayOutputStream();
            ObjectOutputStream theOS = new ObjectOutputStream(theData);
            theOS.writeObject(aEventsToSend);
            theOS.flush();

            byte[] theRawData = theData.toByteArray();
            QUEUE.add(theRawData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Map<String, Object>> receive() {
        if (!QUEUE.isEmpty()) {
            try {
                byte[] theData = QUEUE.take();
                ObjectInputStream theStream = new ObjectInputStream(new ByteArrayInputStream(theData));
                return (List<Map<String, Object>>) theStream.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return EMPTY;
    }
}
