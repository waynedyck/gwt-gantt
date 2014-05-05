package com.imaginedreal.gwtgantt.widget;

import com.imaginedreal.gwtgantt.geometry.Point;

public class SVGPath extends SVGWidget {

	public SVGPath() {
		setElement(SVG.createPath());
	}

	public SVGPath(String path) {
		this();
		setValue(path);
	}

	public void setValue(Point[] points) {

		StringBuilder pointSB = new StringBuilder();
		pointSB.append("M ");
		for (int i = 0; i < points.length; i++) {

			Point point = points[i];
			if(i!=0) pointSB.append(" L ");
			pointSB.append(point.getX());
			pointSB.append(" ");
			pointSB.append(point.getY());
		}
		setValue(pointSB.toString());

	}

	public void setValue(String value) {
		getElement().setAttribute("d", value);
	}

	public String getValue() {
		return getElement().getAttribute("d");
	}

	public void setMarkerStart(SVGMarker value) {
		setMarkerStart(value.getId());
	}
	public void setMarkerStart(String value) {
		getElement().setAttribute("marker-start", "url(#"+value+")");
	}
	public void setMarkerEnd(SVGMarker value) {
		setMarkerEnd(value.getId());
	}
	public void setMarkerEnd(String value) {
		getElement().setAttribute("marker-end", "url(#"+value+")");
	}
}
