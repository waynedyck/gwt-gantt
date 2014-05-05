package com.imaginedreal.gwtgantt.widget;

import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.HasWidgets;

public class SVGDefs extends ComplexPanel implements HasWidgets {

	public SVGDefs() {
		setElement(SVG.createDefs());
	}

	public void add(SVGMarker marker) {
		super.add(marker, getElement());
	}
	
	public void remove(SVGMarker marker) {
		super.remove(marker);
	}
}
