package de.mirkosertic.gameengine.core;

public interface GameSystemWork {

    GameSystemWork NOTHING = new GameSystemWork() {
        @Override
        public void runInFrame() {

        }

        @Override
        public void runAfterFrame() {
        }
    };

    void runInFrame();

    void runAfterFrame();
}
