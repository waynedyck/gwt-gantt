package com.imaginedreal.gwtgantt.widget;

import com.google.gwt.user.client.ui.Widget;

public abstract class SVGWidget extends Widget {

	public void setStroke(String value) {
		getElement().setAttribute("stroke", value);
	}
	
	public void setStrokeWidth(int value) {
		getElement().setAttribute("stroke-width", Integer.toString(value));
	}
	
	public void setFill(String value) {
		getElement().setAttribute("fill", value);
	}
}