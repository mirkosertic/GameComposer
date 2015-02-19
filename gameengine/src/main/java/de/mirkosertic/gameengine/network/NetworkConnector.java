package de.mirkosertic.gameengine.network;

import java.util.List;
import java.util.Map;

public interface NetworkConnector {

    int send(List<Map<String, Object>> aEventsToSend);

    List<Map<String,Object>> receive();
}
