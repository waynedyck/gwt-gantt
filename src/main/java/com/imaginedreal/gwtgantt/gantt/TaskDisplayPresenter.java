/*
 * This file is part of gwt-gantt
 * Copyright (C) 2010  Scottsdale Software LLC
 *
 * gwt-gantt is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/
 */
package com.imaginedreal.gwtgantt.gantt;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SelectionModel;

/**
 * <p>
 * Presenter implementation of {@link HasData} that presents data for various
 * cell based widgets. This class contains most of the shared logic used by
 * these widgets, making it easier to test the common code.
 * <p>
 * <p>
 * In proper MVP design, user code would interact with the presenter. However,
 * that would complicate the widget code. Instead, each widget owns its own
 * presenter and contains its own View. The widget forwards commands through to
 * the presenter, which then updates the widget via the view. This keeps the
 * user facing API simpler.
 * <p>
 *
 * @param <T> the data type of items in the list
 *
 * @author Brad Rydzewski
 */
public abstract class TaskDisplayPresenter<T> implements HasData<T> {

    private HandlerRegistration selectionHandler;
    protected SelectionModel<? super T> selectionModel;
    protected TaskDisplay<T> display;
    protected TaskDisplayView<T> view;
    protected List<? extends T> rowData;

    /**
     * A local cache of the currently selected Items
     */
    private final Set<T> selectedRows = new HashSet<T>();
    
    public void bind(TaskDisplay<T> display, TaskDisplayView<T> view) {
    	this.display = display;
    	this.view = view;
    }

    @Override
    public void setRowData(int start, List<? extends T> values) {
    	this.rowData = values;
    	
    	if(view.isAttached())
    		redraw();
    }
    
    public abstract void redraw();

	public abstract void scrollToItem(T item);

    @Override
    public SelectionModel<? super T> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public void setSelectionModel(SelectionModel<? super T> selectionModel) {

        clearSelectionModel();

        // Set the new selection model.
        this.selectionModel = selectionModel;
        if (selectionModel != null) {
            selectionHandler = selectionModel.addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

                @Override
                public void onSelectionChange(SelectionChangeEvent event) {

                	updateSelection();
                }
            });
        }

        // Update the current selection state based on the new model.
        updateSelection();
    }

    public void clearSelectionModel() {
        if (selectionHandler != null) {
            selectionHandler.removeHandler();
            selectionHandler = null;
        }
        selectionModel = null;
    }

    /**
     * Update the table based on the current selection.
     */
    private void updateSelection() {
    	
//    	boolean selected = selectionModel == null ? false
//    			: selectionModel.isSelected(value);
    	
    	if(rowData==null)
    		return;
//        view.onUpdateSelection();
//
//        // Determine if our selection states are stale.
//        boolean dependsOnSelection = view.dependsOnSelection();
//        boolean refreshRequired = false;
//        ElementIterator children = view.getChildIterator();
//        int row = 0;
        for (T value : rowData) {
//            // Increment the child.
//            if (!children.hasNext()) {
//                break;
//            }
//            children.next();
//
//            // Update the selection state.
        	
            boolean selected = selectionModel == null ? false
                    : selectionModel.isSelected(value);
            
            if (selectedRows.contains(value)) {
                
                if (!selected) {
                    selectedRows.remove(value);
                    view.setSelected(value, false);
                }
            } else if(selected) {
            	view.setSelected(value, true);
            	selectedRows.add(value);
            }
//            row++;
        }
        
        
//
//        // Redraw the entire list if needed.
//        if (refreshRequired && dependsOnSelection) {
//            redraw();
//        }
    }
    
    /**
     * Get the list of data within the current range. The 0th index corresponds to
     * the first value on the page. The data may not be complete or may contain
     * null values.
     *
     * @return the list of data for the current page
     */
    public List<? extends T> getRowData() {
      return rowData;
    }
    
    /**
     * 
     * <p><font color='red'>Unsupported Method:</font>
     * Please use the {@link com.google.gwt.view.client.ListDataProvider} for
     * all Widget data binding.
     * </p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void fireEvent(GwtEvent<?> event) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public int getRowCount() {
        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public Range getVisibleRange() {
//        throw new UnsupportedOperationException();
    	return new Range(0, Integer.MAX_VALUE);
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public boolean isRowCountExact() {
//        throw new UnsupportedOperationException();
    	return true;
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void setRowCount(int count) {
//        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void setRowCount(int count, boolean isExact) {
//        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void setVisibleRange(int start, int length) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void setVisibleRange(Range range) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
        throw new UnsupportedOperationException();
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public HandlerRegistration addRangeChangeHandler(RangeChangeEvent.Handler handler) {
//        throw new UnsupportedOperationException();
    	return null;
    }

    /**
     * <p><font color='red'>This method is not in use, and will
     * throw an UnsupportedOperationException.</font></p>
     *  @throws UnsupportedOperationException
     */
    @Override
    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        throw new UnsupportedOperationException();
    }
}
