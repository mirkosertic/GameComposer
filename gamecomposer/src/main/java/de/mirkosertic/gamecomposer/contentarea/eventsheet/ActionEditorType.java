package de.mirkosertic.gamecomposer.contentarea.eventsheet;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.inject.Qualifier;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Qualifier
public @interface ActionEditorType {
    Class clazz();
}
