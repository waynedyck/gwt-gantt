package com.imaginedreal.gwtgantt.widget;

import java.util.Iterator;
import java.util.NoSuchElementException;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.Widget;

public class SVGMarker extends Panel {

	private Widget widget;

	public SVGMarker(String id) {
		setElement(SVG.createMarker());
		DOM.setElementAttribute(getElement(), "id", id);
	}
	public SVGMarker(String id, double strokeWidth, String stroke, String fill) {
		this(id);
		setStroke(stroke);
		setStrokeWidth(strokeWidth);
		setFill(fill);
	}
	public void setOrient(String value) {
		getElement().setAttribute("orient", value);
	}

	public void setMarkerHeight(int value) {
		getElement().setAttribute("markerHeight", Integer.toString(value));
	}

	public void setMarkerWidth(int value) {
		getElement().setAttribute("markerWidth", Integer.toString(value));
	}

	public void setMarkerUnits(String value) {
		getElement().setAttribute("markerUnits", value);
	}

	public void setRefX(int value) {
		getElement().setAttribute("refX", Integer.toString(value));
	}

	public void setRefY(int value) {
		getElement().setAttribute("refY", Integer.toString(value));
	}

	public void setViewBox(String value) {
		getElement().setAttribute("viewBox", value);
	}

	public String getId() {
		return DOM.getElementAttribute(getElement(), "id");
	}
	
	public void setStroke(String value) {
		getElement().setAttribute("stroke", value);
	}
	
	public void setStrokeWidth(double value) {
		getElement().setAttribute("stroke-width", Double.toString(value));
	}
	
	public void setFill(String value) {
		getElement().setAttribute("fill", value);
	}

	public void addPath(SVGPath path) {
		add(path);
	}
	
	@Override
	public void add(Widget w) {
		// Can't add() more than one widget to a SimplePanel.
		if (getWidget() != null) {
			throw new IllegalStateException(
					"SimplePanel can only contain one child widget");
		}
		setWidget(w);
	}

	/**
	 * Gets the panel's child widget.
	 * 
	 * @return the child widget, or <code>null</code> if none is present
	 */
	public Widget getWidget() {
		return widget;
	}

	public Iterator<Widget> iterator() {
		// Return a simple iterator that enumerates the 0 or 1 elements in this
		// panel.
		return new Iterator<Widget>() {
			boolean hasElement = widget != null;
			Widget returned = null;

			public boolean hasNext() {
				return hasElement;
			}

			public Widget next() {
				if (!hasElement || (widget == null)) {
					throw new NoSuchElementException();
				}
				hasElement = false;
				return (returned = widget);
			}

			public void remove() {
				if (returned != null) {
					SVGMarker.this.remove(returned);
				}
			}
		};
	}

	@Override
	public boolean remove(Widget w) {
		// Validate.
		if (widget != w) {
			return false;
		}

		// Orphan.
		try {
			orphan(w);
		} finally {
			// Physical detach.
			getContainerElement().removeChild(w.getElement());

			// Logical detach.
			widget = null;
		}
		return true;
	}

	/**
	 * Sets this panel's widget. Any existing child widget will be removed.
	 * 
	 * @param w
	 *            the panel's new widget, or <code>null</code> to clear the
	 *            panel
	 */
	public void setWidget(Widget w) {
		// Validate
		if (w == widget) {
			return;
		}

		// Detach new child.
		if (w != null) {
			w.removeFromParent();
		}

		// Remove old child.
		if (widget != null) {
			remove(widget);
		}

		// Logical attach.
		widget = w;

		if (w != null) {
			// Physical attach.
			DOM.appendChild(getContainerElement(), widget.getElement());

			adopt(w);
		}
	}

	protected com.google.gwt.user.client.Element getContainerElement() {
		return getElement();
	}
}
