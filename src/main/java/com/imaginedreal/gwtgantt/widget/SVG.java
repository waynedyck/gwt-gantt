package com.imaginedreal.gwtgantt.widget;

import com.google.gwt.user.client.Element;

public class SVG {
	
	public static final String SVG_NAMESPACE =
		"http://www.w3.org/2000/svg";

	public static final String SVG = "svg";
	public static final String DEFS = "defs";
	public static final String MARKER = "marker";
	public static final String PATH = "path";
	public static final String LINE = "line";
	public static final String POLYLINE = "polyline";
	public static final String RECTANGLE = "rectangle";
	
	public static Element createPolyline() {
		return createElement(POLYLINE);
	}

	public static Element createLine() {
		return createElement(LINE);
	}

	public static Element createRectangle() {
		return createElement(RECTANGLE);
	}
	
	public static Element createSvg() {
		return createElement(SVG);
	}

	public static Element createDefs() {
		return createElement(DEFS);
	}
	
	public static Element createPath() {
		return createElement(PATH);
	}

	public static Element createMarker() {
		return createElement(MARKER);
	}
	
	public static Element createElement(String name) {
		return createElementNS(SVG_NAMESPACE, name);
	}
	
	public static native Element createElementNS(final String ns, 
	        final String name)/*-{
	    return document.createElementNS(ns, name);
	}-*/;
}
