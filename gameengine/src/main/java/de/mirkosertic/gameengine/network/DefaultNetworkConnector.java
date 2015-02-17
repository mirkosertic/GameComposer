package de.mirkosertic.gameengine.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DefaultNetworkConnector implements NetworkConnector {

    protected final static List<Map<String, Object>> EMPTY = new ArrayList<>();

    @Override
    public void send(List<Map<String, Object>> aEventsToSend) {
    }

    @Override
    public List<Map<String, Object>> receive() {
        return EMPTY;
    }
}
