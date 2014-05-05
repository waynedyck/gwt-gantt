package com.imaginedreal.gwtgantt.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ClientBundle.Source;

public interface GridViewResources extends ClientBundle {

	public static final GridViewResources INSTANCE = GWT
			.create(GridViewResources.class);

	@Source("GridViewResources.css")
	TaskGridStyle style();

	@Source("expandArrow.png")
	ImageResource expandArrowImage();
	
	@Source("collapseArrow.png")
	ImageResource collapseArrowImage();

	public interface TaskGridStyle extends CssResource {
		String taskGrid();
		String header();
		String body();
	}
}
