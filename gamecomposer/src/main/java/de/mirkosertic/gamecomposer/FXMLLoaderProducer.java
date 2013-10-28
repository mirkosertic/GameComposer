package de.mirkosertic.gamecomposer;

import javafx.fxml.FXMLLoader;
import javafx.util.Callback;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

public class FXMLLoaderProducer {

    @Inject
    Instance<Object> instance;

    @Produces
    public FXMLLoader createLoader() {
        return new FXMLLoader(null, null, null, new Callback<Class<?>, Object>() {
            public Object call(Class<?> param) {
                return instance.select(param).get();
            }
        }, StandardCharsets.UTF_8);
    }
}
