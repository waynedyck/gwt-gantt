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

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

/**
 *
 * @author Brad Rydzewski
 */
public interface TaskDisplayView<T> extends IsWidget {

    boolean selectionRequiresRefresh();

    void bind(TaskDisplay<T> display);

    void beforeRenderHeaders();
    void beforeRenderTasks();
    void afterRenderHeaders();
    void afterRenderTasks();

 
    void setSelected(T item, boolean selected);

    void renderTask(T item, Rectangle rectangle);

    void renderTaskSummary(T item, Rectangle rectangle);

    void renderTaskMilestone(T item, Rectangle rectangle);

    void renderTaskLabel(T item, Rectangle rectangle);

    void renderConnector(Point[] path);

    void renderTopTimescaleCell(Rectangle bounds, String text);

    void renderBottomTimescaleCell(Rectangle bounds, String text);

    void renderRow(Rectangle bounds, int rowNumber);

    void renderColumn(Rectangle bounds, int dayOfWeek);

    void doTaskSelected(T item);

    void doTaskDeselected(T item);

    void doTaskEnter(T item);

    void doTaskExit(T item);

    void doScroll(int x, int y);

    int getScrollX();

    int getScrollY();

    Rectangle getTaskRectangle(int UID);
    
    boolean isAttached();

    Widget asWidget();
}
