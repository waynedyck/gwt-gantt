package com.imaginedreal.gwtgantt.widget;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

public class SVGPanel extends ComplexPanel implements HasWidgets {


	public SVGPanel() {
		setElement(SVG.createSvg());
	}

	@Override
	public void setWidth(String value) {
		getElement().setAttribute("width", value);
	}

	@Override
	public void setHeight(String value) {
		getElement().setAttribute("height", value);
	}

	public void setPointerEvents(String value) {
		getElement().getStyle().setProperty("pointerEvents", value);
	}

	public void setShapeRendering(String value) {
		getElement().setAttribute("shape-rendering", value);
	}

	@Override
	public void add(Widget w) {
		super.add(w, getElement());
	}

	public void insert(Widget w, int beforeIndex) {
		insert(w, getElement(), beforeIndex, true);
	}
}
