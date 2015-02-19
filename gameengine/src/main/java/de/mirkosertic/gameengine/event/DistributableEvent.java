package de.mirkosertic.gameengine.event;

import java.util.Map;

public interface DistributableEvent {

    String EVENT_ID_ATTRIBUTE = "eventId";

    Map<String, Object> serialize();

    boolean isForced();
}