package com.imaginedreal.gwtgantt.widget;

import com.imaginedreal.gwtgantt.geometry.Point;


public class SVGLine extends SVGWidget {

	public SVGLine() {
		setElement(SVG.createLine());
	}
	
	public void setPoints(Point p1, Point p2) {
		assert(p1!=null);
		assert(p2!=null);
		setPoints(p1.getX(), p1.getY(), p2.getX(), p2.getY());
	}
	
	public void setPoints(int x1, int y1, int x2, int y2) {
		setX1(x1);
		setY1(y1);
		setX2(x2);
		setY2(y2);
	}
	
	public void setX1(int value) {
		getElement().setAttribute("x1", Integer.toString(value));
	}

	public void setY1(int value) {
		getElement().setAttribute("y1", Integer.toString(value));
	}

	public void setX2(int value) {
		getElement().setAttribute("x2", Integer.toString(value));
	}

	public void setY2(int value) {
		getElement().setAttribute("y2", Integer.toString(value));
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
