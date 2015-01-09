package de.mirkosertic.gameengine.dragome;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.dragome.commons.javascript.ScriptHelper;
import com.dragome.helpers.DragomeEntityManager;
import com.dragome.services.RequestExecutorImpl;
import com.dragome.services.interfaces.AsyncCallback;

import de.mirkosertic.gameengine.annotations.InheritedClassInformation;
import de.mirkosertic.gameengine.annotations.ReflectiveMethod;
import de.mirkosertic.gameengine.type.BuiltInFunctions;

@InheritedClassInformation
public class DragomeBuildInFunctions extends BuiltInFunctions
{
    @ReflectiveMethod
	@Override
	public String formatTime(Number aTimeInMilis, String aPattern)
	{
		SimpleDateFormat theFormat= new SimpleDateFormat(aPattern);
		return theFormat.format(new Timestamp(aTimeInMilis.longValue()));
	}

	public static Runnable wrapRunnableForDebugging(final Runnable runnable)
	{
		AsyncCallback<String> asyncCallback= new AsyncCallback<String>()
		{
			@Override
            public void onSuccess(String result)
			{
				runnable.run();
			}

			@Override
            public void onError()
			{
			}
		};

		AsyncCallback<String> wrappedCallback= RequestExecutorImpl.wrapCallback(Serializable.class, asyncCallback);

		Runnable runnable2= new Runnable()
		{
			@Override
            public void run()
			{
				ScriptHelper.eval("window['callback_'+this.javaId].$onSuccess___java_lang_Object$void(new String())", this);
			}
		};

		ScriptHelper.put("callback", wrappedCallback, null);
		ScriptHelper.put("javaId", DragomeEntityManager.getEntityId(runnable2), null);
		ScriptHelper.eval("window['callback_'+javaId]= callback", null);
		return runnable2;
	}

    @Override
    public DragomeBuildInFunctionsClassInformation getClassInformation() {
        return DragomeBuildInFunctionsClassInformation.INSTANCE;
    }
}
