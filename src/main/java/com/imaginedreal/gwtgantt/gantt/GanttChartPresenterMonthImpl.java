package com.imaginedreal.gwtgantt.gantt;

import java.util.Date;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.imaginedreal.gwtgantt.DateUtil;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

public class GanttChartPresenterMonthImpl<T> extends GanttChartPresenter<T> {

    protected static final int ROW1_WIDTH = 448;
    protected static final int ROW1_WIDTH_OFFSET = 450;
    protected static final int ROW2_WIDTH = 148;
    protected static final int ROW2_WIDTH_OFFSET = 150;
    protected static final int ROW1_HEIGHT = 23;
    protected static final int ROW1_HEIGHT_OFFSET = 25;
	protected static final int DAYS_PER_MONTH = 31;
	private static final DateTimeFormat MONTH_FORMAT = DateTimeFormat.getFormat("MMM");
	
	
	protected static final int UNIT_WIDTH = 15;
	protected static final int UNIT_WIDTH_OFFSET = 13;
	
	
    @SuppressWarnings("deprecation")
    protected void renderBackground() {

        Date date = (Date) start.clone();
        int diff = DateUtil.differenceInDays(start, finish);

        
        for(int i=0;i<diff;i++) {
        	
        	//if first day of the month, we draw the month header
        	if(date.getDate()==1) {
        		
        		Date lastDayOfMonth = new Date(date.getYear(),date.getMonth()+1,1,0,0,0);
        		int daysInMonth = DateUtil.differenceInDays(date,lastDayOfMonth);

                int width = daysInMonth * UNIT_WIDTH - 2;
                int height = 24;
                int left = i * UNIT_WIDTH;
                int top = 0;
                Rectangle topTimescaleBounds = new Rectangle(left, top, width, height);
                String topTimescaleString = MONTH_FORMAT.format(date) + " " + (date.getYear()+1900);
                view.renderTopTimescaleCell(topTimescaleBounds, topTimescaleString);
        	}

        
        	//start the week
        	if(date.getDay()==0 || i==0) {
        		int week = DateUtil.getWeekOfYear(date);
                String topTimescaleString = "Week "+week;

                int left = UNIT_WIDTH * i;
                if(i==0 && date.getDay()!=0) {
                	left = UNIT_WIDTH * DateUtil.differenceInDays(date,DateUtil.getFirstDayOfWeek(date)) * -1;
                }
                //ADD Week PANEL
                Rectangle weekHeaderBounds = new Rectangle(left, 0, 7 * UNIT_WIDTH - 2, 25);
                view.renderBottomTimescaleCell(weekHeaderBounds, topTimescaleString);
    		}
        	
        	if (date.getDay() == SATURDAY || date.getDay() == SUNDAY) {
                //ADD BACKGROUND FOR SAT, SUND
                int colTop = 0;
                int colLeft = (UNIT_WIDTH * i);
                int colWidth = UNIT_WIDTH_OFFSET;
                int colHeight = -1; //not used... 100% defined by style
                Rectangle colBounds = new Rectangle(colLeft, colTop, colWidth, colHeight);
                view.renderColumn(colBounds, date.getDay());
            }
        	date = DateUtil.addDays(date, 1);
        }

    }

    
    
    protected void renderTask(T task, int order) {

        int daysFromStart = DateUtil.differenceInDays(start,provider.getStart(task));//+1;
        int daysInLength = DateUtil.differenceInDays(provider.getStart(task), provider.getFinish(task)) + 1;

        daysInLength = Math.max(daysInLength, 1);

        int top = TASK_ROW_HEIGHT * order + TASK_PADDING_TOP;//order * TASK_HEIGHT + ((order+1) * TASK_PADDING) + (order * TASK_PADDING);
        int left = daysFromStart * UNIT_WIDTH;
        int width = daysInLength * UNIT_WIDTH-2;
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
        int left = daysFromStart * UNIT_WIDTH;
        int width = daysInLength * UNIT_WIDTH-2;
//        width = Math.max(width, 15);
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
        int left = daysFromStart * UNIT_WIDTH;
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

    
    
    
    @Override
	public void scrollToItem(T item) {
    	Rectangle rect = view.getTaskRectangle(provider.getUID(item));
    	if(rect != null) {
    		int row = (int)Math.floor(rect.getTop()/TASK_ROW_HEIGHT);
    		int top = (row-1)*TASK_ROW_HEIGHT;
    		int left = rect.getLeft()-UNIT_WIDTH;//IS THIS RIGHT?
    		left = Math.max(0, left);
    		top = Math.max(0, top);
    		view.doScroll(left, top);
    	}
    }

    @SuppressWarnings("deprecation")
    @Override
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
        adjustedStart = new Date(adjustedStart.getYear(),adjustedStart.getMonth(),1);
        return DateUtil.reset(adjustedStart);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected Date calculateFinishDate(List<? extends T> values) {
        Date adjustedFinish = display.getFinish();

        //if the gantt chart's finish date is null, let's set one automatically
        if (adjustedFinish == null) {
            adjustedFinish = new Date();
            adjustedFinish = DateUtil.addDays(adjustedFinish, 7 * 36);
        }

        //if the last task in the gantt chart is after the gantt charts
        // project finish date ...
        if (values != null && !values.isEmpty() && provider.getFinish(values.get(
                values.size() - 1)).after(adjustedFinish)) {

            adjustedFinish = provider.getFinish(values.get(values.size() - 1));
        }

        int y = adjustedFinish.getYear();
        int m = 9;
        switch(adjustedFinish.getMonth()) {
        case 0:
        case 1:
        case 2: m = 0; break;
        case 3:
        case 4:
        case 5: m = 3; break;
        case 6:
        case 7:
        case 8: m = 6; break;
        default : m = 9; break;
        }
        
//        adjustedStart = DateUtil.addDays(adjustedStart, -7);
//        adjustedStart = DateUtil.getFirstDayOfWeek(adjustedStart);
        adjustedFinish = new Date(y,m+3,1);
        return DateUtil.reset(adjustedFinish);
    }

}
