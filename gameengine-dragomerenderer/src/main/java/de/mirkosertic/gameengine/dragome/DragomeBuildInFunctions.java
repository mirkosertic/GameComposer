package de.mirkosertic.gameengine.dragome;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.helpers.DragomeEntityManager;
import com.dragome.services.interfaces.AsyncCallback;
import com.dragome.web.services.RequestExecutorImpl;
import de.mirkosertic.gameengine.type.BuiltInFunctions;
import de.mirkosertic.gameengine.type.BuiltInFunctionsClassInformation;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class DragomeBuildInFunctions extends BuiltInFunctions {

    private static BuiltInFunctionsClassInformation CIINSTANCE = new BuiltInFunctionsClassInformation();

    @Override
    public String formatTime(Number aTimeInMilis, String aPattern) {
        SimpleDateFormat theFormat = new SimpleDateFormat(aPattern);
        return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
    }

    public static Runnable wrapRunnableForDebugging(final Runnable runnable) {
        AsyncCallback<String> asyncCallback = new AsyncCallback<String>() {
            @Override
            public void onSuccess(String result) {
                runnable.run();
            }

            @Override
            public void onError() {
            }
        };

        AsyncCallback<String> wrappedCallback = RequestExecutorImpl.wrapCallback(Serializable.class, asyncCallback);

        Runnable runnable2 = new Runnable() {
            @Override
            public void run() {
                ScriptHelper.eval("window['callback_'+this.javaId].$onSuccess___java_lang_Object$void(new String())", this);
            }
        };

        ScriptHelper.put("callback", wrappedCallback, null);
        ScriptHelper.put("javaId", DragomeEntityManager.getEntityId(runnable2), null);
        ScriptHelper.eval("window['callback_'+javaId]= callback", null);
        return runnable2;
    }

    @Override
    public BuiltInFunctionsClassInformation getClassInformation() {
        return CIINSTANCE;
    }
}
