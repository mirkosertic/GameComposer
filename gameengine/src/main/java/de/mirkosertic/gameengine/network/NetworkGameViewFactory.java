package de.mirkosertic.gameengine.network;

import de.mirkosertic.gameengine.event.GameEvent;
import de.mirkosertic.gameengine.event.GameEventManager;

public class NetworkGameViewFactory {

    private final NetworkConnector networkConnector;

    public NetworkGameViewFactory(NetworkConnector aConnector) {
        networkConnector = aConnector;
    }

    public NetworkGameView createNetworkViewFor(GameEventManager aEventManager) {
        NetworkGameView theView = new NetworkGameView(networkConnector);
        aEventManager.register(null, GameEvent.class, theView);
        return theView;
    }
}