package com.imaginedreal.gwtgantt.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface GanttResources extends ClientBundle {
	
	public static final GanttResources INSTANCE =  GWT.create(GanttResources.class);
	
	@Source("Task.css")
	TaskStyle taskStyle();

	@Source("arrowBlue.gif")
	ImageResource blueArrowImage();
	@Source("arrowRed.gif")
	ImageResource redArrowImage();
	@Source("arrowGreen.gif")
	ImageResource greenArrowImage();
	
	@Source("arrowPurple.gif")
	ImageResource purpleArrowImage();
	@Source("arrowOrange.gif")
	ImageResource orangeArrowImage();
	
	public interface TaskStyle extends CssResource {
		
	}
}
