package com.imaginedreal.gwtgantt.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;

public interface GridResources extends ClientBundle {
	
	public static final GridResources INSTANCE =  GWT.create(GridResources.class);
	
	@Source("Grid.css")
	GridStyle style();
	
	@Source("expandArrow.png")
	ImageResource expandArrowImage();
	
	@Source("collapseArrow.png")
	ImageResource collapseArrowImage();
	
	public interface GridStyle extends CssResource {
		
	}
}
