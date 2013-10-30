package de.mirkosertic.gameengine.core;

public class GameObjectInstanceFactory {

    private GameRuntime gameRuntime;

    public GameObjectInstanceFactory(GameRuntime aGameRuntime) {
        gameRuntime = aGameRuntime;
    }

    public GameObjectInstance createFrom(GameObject aGameObject) {
        GameObjectInstance theInstance = new GameObjectInstance();
        for (GameComponentTemplate theFactory : aGameObject.getComponentTemplates()) {
            theInstance.addComponent(theFactory.create(theInstance, gameRuntime));
        }
        return theInstance;
    }
}