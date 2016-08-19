/*
 * Copyright 2016 Mirko Sertic
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
