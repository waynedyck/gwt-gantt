package com.imaginedreal.gwtgantt.widget;

import com.imaginedreal.gwtgantt.geometry.Point;


public class SVGPolyLine extends SVGWidget {

	private Point[] points;
	
	public SVGPolyLine() {
		setElement(SVG.createPolyline());
	}
	
	public void setPoints(Point[] points) {
		this.points = points;
		StringBuilder pointSB = new StringBuilder();
		for(int i=0;i<points.length;i++) {
			if(i!=0) {
				pointSB.append(" ");
			}
			
			Point point = points[i];
			pointSB.append(point.getX());
			pointSB.append(",");
			pointSB.append(point.getY());
		}
		getElement().setAttribute("points", pointSB.toString());	
	}
	
	public Point[] getPoints() {
		return points;
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
