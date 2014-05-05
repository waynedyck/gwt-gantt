package com.imaginedreal.gwtgantt.gantt;

import java.util.Date;
import java.util.List;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.imaginedreal.gwtgantt.DateUtil;
import com.imaginedreal.gwtgantt.geometry.Rectangle;

public class GanttChartPresenterYearImpl<T> extends GanttChartPresenter<T> {

    protected static final int ROW1_WIDTH = 448;
    protected static final int ROW1_WIDTH_OFFSET = 450;
    protected static final int ROW2_WIDTH = 148;
    protected static final int ROW2_WIDTH_OFFSET = 150;
    protected static final int ROW1_HEIGHT = 23;
    protected static final int ROW1_HEIGHT_OFFSET = 25;
	protected static final int DAYS_PER_MONTH = 31;
	private static final DateTimeFormat MONTH_FORMAT = DateTimeFormat.getFormat("MMM");
	
	
	protected static final int UNIT_WIDTH = 2;
	protected static final int UNIT_WIDTH_OFFSET = 1;
	
	
    @SuppressWarnings("deprecation")
    protected void renderBackground() {

        Date date = (Date) start.clone();
        int diff = DateUtil.differenceInDays(start, finish);

        
        for(int i=0;i<diff;i++) {
        	
        	//if first day of the month, we draw the month header
        	if((date.getDate()==1 && date.getMonth() % 3==0) || i==0) {
        		
        		Date lastDayOfQtr = new Date(date.getYear(),date.getMonth()+3,1,0,0,0);
        		int daysInQtr = DateUtil.differenceInDays(date,lastDayOfQtr);
        		int quarter = (date.getMonth() / 3)+1;
        		
                int width = daysInQtr * UNIT_WIDTH - 2;
                int height = 24;
                int left = i * UNIT_WIDTH;
                int top = 0;
                
//                if(i==0 && date.getMonth()%3!=0) {
//                	left = UNIT_WIDTH * DateUtil.differenceInDays(date,DateUtil.g(date)) * -1;
//                }
                
                Rectangle bottomTimescaleBounds = new Rectangle(left, top, width, height);
                String bottomTimescaleString = "Q"+quarter;
                view.renderBottomTimescaleCell(bottomTimescaleBounds, bottomTimescaleString);
        	
        	
                if(date.getMonth()==0) {
                	
            		Date lastDayOfYear = new Date(date.getYear()+1,0,1,0,0,0);
            		int daysInYear = DateUtil.differenceInDays(date,lastDayOfYear);
            		width = daysInYear * UNIT_WIDTH - 2;
                	
//                    if(i==0 && date.getMonth()!=0) {
//                    	left = left * -1;
//                    }
            		
//                	left = i * UNIT_WIDTH;
                    Rectangle topTimescaleBounds = new Rectangle(left, 0, width, 25);
                    String topTimescaleString = String.valueOf(1900 + date.getYear());
                    view.renderTopTimescaleCell(topTimescaleBounds, topTimescaleString);
                }
        	}

        
//        	//start the week
//        	if(date.getDay()==0 || i==0) {
//        		int week = DateUtil.getWeekOfYear(date);
//                String topTimescaleString = "Week "+week;
//
//                int left = UNIT_WIDTH * i;
//                if(i==0 && date.getDay()!=0) {
//                	left = UNIT_WIDTH * DateUtil.differenceInDays(date,DateUtil.getFirstDayOfWeek(date)) * -1;
//                }
//                //ADD Week PANEL
//                Rectangle weekHeaderBounds = new Rectangle(left, 0, 7 * UNIT_WIDTH - 2, 25);
//                view.renderBottomTimescaleCell(weekHeaderBounds, topTimescaleString);
//    		}

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
        width = Math.max(width, 15);
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

//        int month = (int)Math.floor(adjustedStart.getMonth()/4d);
        adjustedStart = new Date(adjustedStart.getYear(),0,1);
        return DateUtil.reset(adjustedStart);
    }

    @SuppressWarnings("deprecation")
	@Override
    protected Date calculateFinishDate(List<? extends T> values) {
        Date adjustedFinish = display.getFinish();

        //if the gantt chart's finish date is null, let's set one automatically
        if (adjustedFinish == null) {
            adjustedFinish = new Date();
            adjustedFinish = DateUtil.addDays(adjustedFinish, 365*3); //3 years
        }

        //if the last task in the gantt chart is after the gantt charts
        // project finish date ...
        if (values != null && !values.isEmpty() && provider.getFinish(values.get(
                values.size() - 1)).after(adjustedFinish)) {

            adjustedFinish = provider.getFinish(values.get(values.size() - 1));
        }

        
//        adjustedStart = DateUtil.addDays(adjustedStart, -7);
//        adjustedStart = DateUtil.getFirstDayOfWeek(adjustedStart);
        adjustedFinish = new Date(adjustedFinish.getYear(),0,1);
        return DateUtil.reset(adjustedFinish);
    }

}
