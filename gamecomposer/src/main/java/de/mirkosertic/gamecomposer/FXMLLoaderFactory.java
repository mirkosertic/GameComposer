package de.mirkosertic.gamecomposer;

import javafx.fxml.FXMLLoader;
import javafx.fxml.JavaFXBuilderFactory;
import javafx.util.Callback;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FXMLLoaderFactory {

    @Inject
    Instance<Object> instance;

    public FXMLLoader createLoader() {
        FXMLLoader theLoader = new FXMLLoader();
        theLoader.setBuilderFactory(new JavaFXBuilderFactory());
        theLoader.setControllerFactory(new Callback<Class<?>, Object>() {
            public Object call(Class<?> param) {
                return instance.select(param).get();
            }
        });
        return theLoader;
    }
}