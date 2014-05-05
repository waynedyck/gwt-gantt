package com.imaginedreal.gwtgantt.table;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.CellTable.Resources;

public interface CellTableResources extends Resources {
	
	public CellTableResources INSTANCE =
		GWT.create(CellTableResources.class);
	
	/**
	 * The styles used in this widget.
	 */
	@Source("CellTableResources.css")
	CellTable.Style cellTableStyle();
}
