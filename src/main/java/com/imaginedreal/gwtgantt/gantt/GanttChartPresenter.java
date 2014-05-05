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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.view.client.CellPreviewEvent.Handler;
import com.imaginedreal.gwtgantt.DateUtil;
import com.imaginedreal.gwtgantt.connector.CalculatorFactory;
import com.imaginedreal.gwtgantt.geometry.Point;
import com.imaginedreal.gwtgantt.geometry.Rectangle;
import com.imaginedreal.gwtgantt.model.PredecessorType;
import com.imaginedreal.gwtgantt.model.Task;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Brad Rydzewski
 */
public class GanttChartPresenter<T> extends TaskDisplayPresenter<T> {

    protected static final int ROW1_WIDTH = 278;
    protected static final int ROW1_WIDTH_OFFSET = 280;
    protected static final int ROW1_HEIGHT = 23;
    protected static final int ROW1_HEIGHT_OFFSET = 25;
    protected static final int ROW2_WIDTH = 38;
    protected static final int ROW2_WIDTH_OFFSET = 40;
    protected static final int ROW2_HEIGHT = 23;
    protected static final int ROW2_HEIGHT_OFFSET = 25;
    protected static final int TASK_ROW_HEIGHT = 24;
    protected static final int TASK_HEIGHT = 10;
    protected static final int TASK_PADDING_TOP = 6;
    protected static final int MILESTONE_WIDTH = 16;
    protected static final int MILESTONE_PADDING_TOP = 4;
    protected static final int SUMMARY_HEIGHT = 7;
    protected static final int SUMMARY_PADDING_TOP = 6;
    public static final int SATURDAY = 6;
    public static final int SUNDAY = 0;
    protected Date start;
    protected Date finish;
    protected ProvidesTask<T> provider;


    
    @Override
    public void redraw() {
   		
        //local copy of the task provider
        provider = display.getProvidesTask();


        Date newStart = calculateStartDate(rowData);
        Date newFinish = calculateFinishDate(rowData);

        
//        if(start==null ||
//        		finish==null) {// ||
//        		!DateUtil.areOnTheSameDay(start, newStart) ||
//        		!DateUtil.areOnTheSameDay(finish, newFinish)) {

        	this.start = newStart;
            this.finish = newFinish;

        	view.beforeRenderHeaders();
        	renderBackground();
        	view.afterRenderHeaders();
//        }

        view.beforeRenderTasks();
        if(rowData!=null && !rowData.isEmpty()) {
            
            renderTasks(rowData);
            renderConnectors(rowData);
        }
        view.afterRenderTasks();
    }

    @SuppressWarnings("deprecation")
    protected void renderBackground() {

        Date date = (Date) start.clone();

        int diff = DateUtil.differenceInDays(start, finish);
        int weeks = (int) Math.ceil(diff / 7);

        // FOR EACH WEEK
        for (int i = 0; i < weeks; i++) {

            //TODO: Remove reference to DateTimeFormat, which requires GWT Test Case
            Date firstDayOfWeek = DateUtil.getFirstDayOfWeek(date);
            String topTimescaleString = DateTimeFormat.getMediumDateFormat().format(firstDayOfWeek);

            //ADD WEEK PANEL
            Rectangle weekHeaderBounds = new Rectangle(ROW1_WIDTH_OFFSET * i, 0, ROW1_WIDTH, 25);
            view.renderTopTimescaleCell(weekHeaderBounds, topTimescaleString);

            // ADD 7 DAYS PER WEEK
            for (int d = 0; d < DateUtil.DAYS_PER_WEEK; d++) {
                //ADD DAY PANEL
                Date weekDay = DateUtil.addDays(date, d);
                int width = ROW2_WIDTH;
                int height = 24;
                int left = (ROW1_WIDTH_OFFSET * i) + (ROW2_WIDTH_OFFSET * d);
                int top = 0;
                Rectangle bottomTimescaleBounds = new Rectangle(left, top, width, height);
                String bottomTimescaleString = weekDay.getDate() + "";
                view.renderBottomTimescaleCell(bottomTimescaleBounds, bottomTimescaleString);

                if (d == SATURDAY || d == SUNDAY) {
                    //ADD BACKGROUND FOR SAT, SUND
                    int colTop = 0;
                    int colLeft = (ROW1_WIDTH_OFFSET * i) + (ROW2_WIDTH_OFFSET * d);
                    int colWidth = 38;
                    int colHeight = -1; //not used... 100% defined by style
                    Rectangle colBounds = new Rectangle(colLeft, colTop, colWidth, colHeight);
                    view.renderColumn(colBounds, d);
                }
            }

            date = DateUtil.addDays(date, DateUtil.DAYS_PER_WEEK);
        }
    }

    protected void renderTasks(List<? extends T> values) {


        int count = 0;
        boolean collapse = false;
        int collapseLevel = -1;

        for (int i = 0; i < values.size(); i++) {
            //get the task
            T task = values.get(i);

            collapse = collapse && provider.getLevel(task) >= collapseLevel;

            if (!collapse) {
                if (provider.isSummary(task)) {
                    //render the summary widget
                    renderTaskSummary(task, count);
                    collapse = provider.isCollapsed(task);
                    collapseLevel = provider.getLevel(task) + 1;
                } else if (provider.isMilestone(task)) {
                    //render the milestone widget
                    renderTaskMilestone(task, count);
                } else {
                    //render the task widget
                    renderTask(task, count);
                }
                count++;
            }
        }
    }

    protected void renderTask(T task, int order) {

        int daysFromStart = DateUtil.differenceInDays(start,provider.getStart(task));//+1;
        int daysInLength = DateUtil.differenceInDays(provider.getStart(task), provider.getFinish(task)) + 1;

        daysInLength = Math.max(daysInLength, 1);

        int top = TASK_ROW_HEIGHT * order + TASK_PADDING_TOP;//order * TASK_HEIGHT + ((order+1) * TASK_PADDING) + (order * TASK_PADDING);
        int left = daysFromStart * ROW2_WIDTH_OFFSET;
        int width = daysInLength * ROW2_WIDTH_OFFSET - 4;
        int height = TASK_HEIGHT;
        GWT.log("proj start: " + start + "  task start: "+provider.getStart(task) + "   daysFromDuration: "+daysFromStart);

        //render the task
        Rectangle taskBounds = new Rectangle(left, top, width, height);
        view.renderTask(task, taskBounds);

        //render the label
        Rectangle labelBounds = new Rectangle(taskBounds.getRight(), top - 2, -1, -1);
        view.renderTaskLabel(task, labelBounds);

        //if task is selected, make sure it is rendered as selected
        if (selectionModel.isSelected(task)) {
            view.doTaskSelected(task);
        }
    }

    protected void renderTaskSummary(T task, int order) {

        int daysFromStart = DateUtil.differenceInDays(start, provider.getStart(task));//+1
        int daysInLength = DateUtil.differenceInDays(provider.getStart(task), provider.getFinish(task)) + 1;

        daysInLength = Math.max(daysInLength, 1);

        int top = TASK_ROW_HEIGHT * order + SUMMARY_PADDING_TOP;
        //order * TASK_HEIGHT + ((order+1) * TASK_PADDING) + (order * TASK_PADDING);
        int left = daysFromStart * ROW2_WIDTH_OFFSET;
        int width = daysInLength * ROW2_WIDTH_OFFSET - 4;
        int height = SUMMARY_HEIGHT;

        //render the task
        Rectangle taskBounds = new Rectangle(left, top, width, height);
        view.renderTaskSummary(task, taskBounds);

        //render the label
        Rectangle labelBounds = new Rectangle(taskBounds.getRight(), top - 2, -1, -1);
        view.renderTaskLabel(task, labelBounds);

        //if task is selected, make sure it is rendered as selected
        if (selectionModel.isSelected(task)) {
            view.doTaskSelected(task);
        }
    }

    protected void renderTaskMilestone(T task, int order) {

        int daysFromStart = DateUtil.differenceInDays(start, provider.getStart(task));//+1;

        int top = TASK_ROW_HEIGHT * order + MILESTONE_PADDING_TOP;//order * TASK_HEIGHT + ((order+1) * TASK_PADDING) + (order * TASK_PADDING);
        int left = daysFromStart * ROW2_WIDTH_OFFSET;
        int width = MILESTONE_WIDTH;
        int height = TASK_HEIGHT;

        //render the task
        Rectangle taskBounds = new Rectangle(left, top, width, height);
        view.renderTaskMilestone(task, taskBounds);

        //render the label
        Rectangle labelBounds = new Rectangle(taskBounds.getRight(), top - 2, -1, -1);
        view.renderTaskLabel(task, labelBounds);

        //if task is selected, make sure it is rendered as selected
        if (selectionModel.isSelected(task)) {
            view.doTaskSelected(task);
        }
    }

    protected void renderConnectors(List<? extends T> values) {

        for (int i = 0; i < values.size(); i++) {

            T task = values.get(i);
            for (int p=0;p<provider.getPredecessorCount(task);p++) {
                int predecessorUID = provider.getPredecessorUID(task, p);
                Point[] path = null;
                Rectangle fromRect = view.getTaskRectangle(predecessorUID);
                Rectangle toRect = view.getTaskRectangle(provider.getUID(task));
                PredecessorType type = provider.getPredecessorType(task, p);
                if (fromRect != null && toRect != null) {
                    path = CalculatorFactory.get(type).calculateWithOffset(fromRect, toRect);
                    renderConnector(path);
                    
//                    if(i==2){
//                    	System.out.println(" uid:  " + predecessorUID);
//                    	System.out.println(" from: " + fromRect.toString());
//                    	System.out.println(" to:   " + toRect.toString());
//                    	System.out.println(" path: " + path[0].toString());
//                    	
//                    }
                }

            }

        }

    }

    protected void renderConnector(Point[] path) {
        view.renderConnector(path);
    }

    protected Date calculateStartDate(List<? extends T> values) {

        Date adjustedStart = display.getStart();

        //if the gantt chart's start date is null, let's set one automatically
        if (display.getStart() == null) {
            adjustedStart = new Date();
        }

        //if the first task in the gantt chart is before the gantt charts
        // project start date ...
        if (values != null && !values.isEmpty()
                && provider.getStart(values.get(0)).before(adjustedStart)) {

            adjustedStart = provider.getStart(values.get(0));
        }

        adjustedStart = DateUtil.addDays(adjustedStart, -7);
        adjustedStart = DateUtil.getFirstDayOfWeek(adjustedStart);
        return DateUtil.reset(adjustedStart);
    }

    protected Date calculateFinishDate(List<? extends T> values) {

        Date adjustedFinish = display.getFinish();

        //if the gantt chart's finish date is null, let's set one automatically
        if (adjustedFinish == null) {
            adjustedFinish = new Date();
            adjustedFinish = DateUtil.addDays(adjustedFinish, 7 * 12);
        }

        //if the last task in the gantt chart is after the gantt charts
        // project finish date ...
        if (values != null && !values.isEmpty() && provider.getFinish(values.get(
                values.size() - 1)).after(adjustedFinish)) {

            adjustedFinish = provider.getFinish(values.get(values.size() - 1));
        }

        adjustedFinish = DateUtil.addDays(adjustedFinish, 7);
        adjustedFinish = DateUtil.getFirstDayOfWeek(adjustedFinish);
        return DateUtil.reset(adjustedFinish);
    }

    @Override
	public void scrollToItem(T item) {
    	Rectangle rect = view.getTaskRectangle(provider.getUID(item));
    	if(rect != null) {
    		int row = (int)Math.floor(rect.getTop()/TASK_ROW_HEIGHT);
    		int top = (row-1)*TASK_ROW_HEIGHT;
    		int left = rect.getLeft()-ROW2_WIDTH;
    		left = Math.max(0, left);
    		top = Math.max(0, top);
    		view.doScroll(left, top);
    	}
    }

    @Override
    public T getVisibleItem(int indexOnPage) {

        return this.getVisibleItem(indexOnPage);
    }

    @Override
    public int getVisibleItemCount() {

        return this.getVisibleItemCount();
    }

    @Override
    public Iterable<T> getVisibleItems() {

        return this.getVisibleItems();
    }

    @Override
    public HandlerRegistration addCellPreviewHandler(Handler<T> handler) {

        return this.addCellPreviewHandler(handler);
    }
}
