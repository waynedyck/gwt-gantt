package com.imaginedreal.gwtgantt.table;

import com.google.gwt.user.cellview.client.Column;
import com.imaginedreal.gwtgantt.model.Task;

public class TaskGridColumn<T> {
	
	private String name;
	private String style;
	private int width = 100;
	private final Column<T, ?> column;
	
	public TaskGridColumn(Column<T, ?> column) {
		this(column,"",100);
	}

	public TaskGridColumn(Column<T, ?> column, String name, int width) {
		this(column,name,width,null);
	}
	
	public TaskGridColumn(Column<T, ?> column, String name, int width, String style) {
		this.column = column;
		this.name = name;
		this.width = width;
		this.style = style;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public Column<T, ?> getColumn() {
		return column;
	}
	public String getStyle() {
		return style;
	}
	public void setStyle(String style) {
		this.style = style;
	}
}
