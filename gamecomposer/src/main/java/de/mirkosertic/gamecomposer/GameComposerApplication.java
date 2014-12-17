package de.mirkosertic.gamecomposer;

import javafx.application.Application;
import javafx.stage.Stage;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;

public class GameComposerApplication extends Application {

    private Weld weld;
    private WeldContainer weldContainer;

    @Override
    public void start(Stage aStage) throws Exception {
        weld = new Weld();
        weldContainer = weld.initialize();
        weldContainer.instance().select(GameComposerFactory.class).get().createAndStart(this, aStage, getParameters());
    }

    @Override
    public void stop() throws Exception {
        weldContainer.event().select(ShutdownEvent.class).fire(new ShutdownEvent());
        super.stop();
        weld.shutdown();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
