/*******************************************************************************
 * Copyright (c) 2011-2014 Fernando Petrola
 * 
 *  This file is part of Dragome SDK.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package group.gui;

import group.service.HelloWorldService;

import com.dragome.annotations.PageAlias;
import com.dragome.debugging.execution.DragomeVisualActivity;
import com.dragome.model.VisualButtonImpl;
import com.dragome.model.VisualLabelImpl;
import com.dragome.model.interfaces.VisualButton;
import com.dragome.model.interfaces.VisualComponent;
import com.dragome.model.interfaces.VisualLabel;
import com.dragome.model.listeners.ClickListener;

@PageAlias(alias= "hello-world")
public class HelloWorldPage extends DragomeVisualActivity
{
	HelloWorldService helloWorldService= serviceFactory.createSyncService(HelloWorldService.class);

	public void build()
	{
		final VisualLabel<String> label= new VisualLabelImpl<String>("message");
		final VisualButton button= new VisualButtonImpl("button", new ClickListener()
		{
		    public void clickPerformed(VisualComponent aVisualComponent)
		    {
			label.setValue(helloWorldService.getGreetingsFor("World"));
		    }
		});
		
		mainPanel.addChild(label);
		mainPanel.addChild(button);
	}
}
