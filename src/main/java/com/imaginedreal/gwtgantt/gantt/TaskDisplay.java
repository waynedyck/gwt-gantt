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

import java.util.Date;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SelectionModel;
import com.imaginedreal.gwtgantt.model.ZoomLevel;

/**
 *
 * @author Brad Rydzewski
 */
public interface TaskDisplay<T> extends HasData<T>, IsWidget {

    /**
     * Get the start of the Date range.
     *
     * @return the start of the Date range.
     */
    public Date getStart();

    /**
     * Set the start of the Date range.
     *
     * @param start
     *            the start of the Date range.
     */
    public void setStart(Date start);

    /**
     * Get the end of the Date range.
     *
     * @return the end of the Date range.
     */
    public Date getFinish();

    /**
     * Set the end of the Date range.
     *
     * @param finish
     *            the end of the Date range.
     */
    public void setFinish(Date finish);

    /**
     * Sets the Selected Item.
     * @param item Selected Item.
     */
    public void setSelectedItem(T item, boolean selected);

    /**
     * Forces the Display to scroll to the Item.
     * @param item Item to scroll to.
     */
    public void scrollToItem(T item);

    /**
     * 
     * @return
     */
    public ProvidesTask<T> getProvidesTask();
    
//    public boolean isAttached();

//	public void fireItemClickEvent(T item, Point point);
//
//	public void fireItemDoubleClickEvent(T item, Point point);
//
//        public void fireItemEnterEvent(T item, Rectange bounds);
//
//        public void fireItemExitEvent(T item, Rectange bounds);


  /**
   * Get the {@link SelectionModel} used by this {@link HasData}.
   *
   * @return the {@link SelectionModel}
   *
   * @see #setSelectionModel(SelectionModel)
   */
    @Override
    SelectionModel<? super T> getSelectionModel();
    
    
    void setZoomLevel(ZoomLevel zoom);
    
    ZoomLevel getZoomLevel();
    
    boolean zoomOut();
    boolean zoomIn();
}
