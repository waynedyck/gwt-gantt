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
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.RowCountChangeEvent;
import com.google.gwt.view.client.SelectionModel;
import com.imaginedreal.gwtgantt.model.ZoomLevel;
import com.imaginedreal.gwtgantt.resources.GanttResources;

/**
 *
 * @author Brad Rydzewski
 */
public class GanttChart<T> extends ResizeComposite implements HasScrollHandlers, TaskDisplay<T> {

	class SimplePanelWithResize extends SimplePanel implements RequiresResize {
		SimplePanelWithResize() {
			getElement().getStyle().setWidth(100, Unit.PCT);
			getElement().getStyle().setHeight(100, Unit.PCT);
		}
		@Override
		public void onResize() {
			if(getWidget() instanceof RequiresResize) {
				((RequiresResize)getWidget()).onResize();
			}
		}
	}

	private final Scheduler.ScheduledCommand redrawCommand =
		new Scheduler.ScheduledCommand() {
        @Override
		public void execute() {
			presenter.redraw();
	    }
	};
	
    private SimplePanelWithResize root = new SimplePanelWithResize();
    private final ProvidesTask<T> taskProvider;
    private TaskDisplayPresenter<T> presenter;
    private TaskDisplayPresenter<T> quarterPresenter = GWT.create(GanttChartPresenterQuarterImpl.class);
    private TaskDisplayPresenter<T> yearPresenter = GWT.create(GanttChartPresenterYearImpl.class);
    private TaskDisplayPresenter<T> monthPresenter = GWT.create(GanttChartPresenterMonthImpl.class);
    private TaskDisplayPresenter<T> dayPresenter = GWT.create(GanttChartPresenterDayImpl.class);
    private TaskDisplayPresenter<T> weekPresenter = GWT.create(GanttChartPresenter.class);
    private TaskDisplayView<T> view = GWT.create(GanttChartView.class);
    private Date start;
    private Date finish;
//    private boolean dirty = false;
//    private boolean loaded = false;


    public GanttChart(
    		//TaskDisplayPresenter<T> presenter,
            //TaskDisplayView<T> view,
            ProvidesTask<T> taskProvider) {

        initWidget(root);
        
        GanttResources.INSTANCE.taskStyle().ensureInjected();
        
        this.presenter = weekPresenter;
        this.taskProvider = taskProvider;
//        this.view = view;

        this.view.bind(this);
        this.presenter.bind(this, view);
        root.add(this.view.asWidget());//Widget asWidget();
    }


    @Override
    public final Date getStart() {
        return start;
    }

    @Override
    public void setStart(Date start) {
        this.start = start;
    }

    @Override
    public final Date getFinish() {
        return finish;
    }

    @Override
    public void setFinish(Date finish) {
        this.finish = finish;
    }

    @Override
    public void setSelectedItem(T item, boolean selected) {
        view.setSelected(item, selected);
        //TODO: should we really make the view keep track of positioning?
    }
    


    @Override
    public void scrollToItem(T item) {
    	presenter.scrollToItem(item);
    }

    public void scrollTo(int x, int y) {
        view.doScroll(x, y);
    }


    @Override
    public void onLoad() {
//    	presenter.redraw();
    	
    	redrawCommand.execute();
    }


    @Override
    public SelectionModel<? super T> getSelectionModel() {
        return presenter.getSelectionModel();
    }

    @Override
    public void setRowData(int start, List<? extends T> values) {
    	presenter.setRowData(start, values);
    }

    @Override
    public void setSelectionModel(SelectionModel<? super T> selectionModel) {
        presenter.setSelectionModel(selectionModel);
    }

    @Override
    public void setVisibleRangeAndClearData(Range range, boolean forceRangeChangeEvent) {
        presenter.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
    }

    @Override
    public HandlerRegistration addRangeChangeHandler(Handler handler) {
        return presenter.addRangeChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addRowCountChangeHandler(RowCountChangeEvent.Handler handler) {
        return presenter.addRowCountChangeHandler(handler);
    }

    @Override
    public HandlerRegistration addScrollHandler(ScrollHandler handler) {
        return super.getWidget().addHandler(handler, ScrollEvent.getType());
    }

    @Override
    public int getRowCount() {
        return presenter.getRowCount();
    }

    @Override
    public Range getVisibleRange() {
        return presenter.getVisibleRange();
    }

    @Override
    public boolean isRowCountExact() {
        return presenter.isRowCountExact();
    }

    @Override
    public void setRowCount(int count) {
        presenter.setRowCount(count);
    }

    @Override
    public void setRowCount(int count, boolean isExact) {
        presenter.setRowCount(count, isExact);
    }

    @Override
    public void setVisibleRange(int start, int length) {
        presenter.setVisibleRange(start, length);
    }

    @Override
    public void setVisibleRange(Range range) {
        presenter.setVisibleRange(range);
    }

    @Override
    public void fireEvent(GwtEvent<?> event) {
//        presenter.fireEvent(event);
        super.getWidget().fireEvent(event);
    }

    @Override
    public ProvidesTask<T> getProvidesTask() {
        return taskProvider;
    }

    public void redraw() {
        presenter.redraw();
    }

    public int getScrollX() {
        return view.getScrollX();
    }

    public int getScrollY() {
        return view.getScrollY();
    }
    



	@Override
	public void setZoomLevel(ZoomLevel zoom) {
		
		TaskDisplayPresenter<T> oldPresenter = this.presenter;
		
		switch(zoom) {
		case Year:    this.presenter = yearPresenter; break;
		case Quarter: this.presenter = quarterPresenter; break;
		case Month:   this.presenter = monthPresenter; break;
		case Default: 
		case Week: this.presenter = weekPresenter; break;
		case Day: this.presenter = dayPresenter; break;
		}
		
		if(oldPresenter!=null) {
			this.presenter.bind(this, view);
			this.presenter.setSelectionModel(oldPresenter.getSelectionModel());
			this.presenter.setRowData(0, oldPresenter.getRowData());
		} else {
			
		}

	}


	@Override
	public ZoomLevel getZoomLevel() {
		if(presenter instanceof GanttChartPresenterYearImpl) {
			return ZoomLevel.Year;
		} else if(presenter instanceof GanttChartPresenterQuarterImpl) {
			return ZoomLevel.Quarter;
		} else if(presenter instanceof GanttChartPresenterMonthImpl) {
			return ZoomLevel.Month;
		} else if(presenter instanceof GanttChartPresenterDayImpl) {
			return ZoomLevel.Day;
		} else if(presenter instanceof GanttChartPresenter) {
			return ZoomLevel.Week;
		} else return ZoomLevel.Default;
	}
	
	public boolean zoomIn() {
		ZoomLevel currentZoom = getZoomLevel();
		ZoomLevel newZoom = currentZoom;
		
		switch(currentZoom) {
		case Year: newZoom = ZoomLevel.Quarter; break;
		case Quarter: newZoom = ZoomLevel.Month; break;
		case Month: newZoom = ZoomLevel.Week; break;
		case Default: newZoom = ZoomLevel.Day; break;
		case Week: newZoom = ZoomLevel.Day; break;
		case Day: break;
		default: break; //do nothing, can't zoom in any further
		}
//		System.out.println("zoomIn(), current zoom: "+currentZoom + "  new zoom: "+newZoom);
		if(newZoom != currentZoom) {
			setZoomLevel(newZoom);
		}
		
		return currentZoom != newZoom;
	}

	public boolean zoomOut() {
		ZoomLevel currentZoom = getZoomLevel();
		ZoomLevel newZoom = currentZoom;
		
		switch(currentZoom) {
		case Year: break;
		case Quarter:  newZoom = ZoomLevel.Year; break;
		case Month: newZoom = ZoomLevel.Quarter; break;
		case Default : newZoom = ZoomLevel.Month; break;
		case Week: newZoom = ZoomLevel.Month; break;
		case Day: newZoom = ZoomLevel.Week; break;
		}
//		System.out.println("zoomOut(), current zoom: "+currentZoom + "  new zoom: "+newZoom);
		
		if(newZoom != currentZoom) {
			setZoomLevel(newZoom);
		}
		
		return currentZoom != newZoom;
	}


    @Override
    public T getVisibleItem(int indexOnPage) {
        return presenter.getVisibleItem(indexOnPage);
    }


    @Override
    public int getVisibleItemCount() {
        return presenter.getVisibleItemCount();
    }


    @Override
    public Iterable<T> getVisibleItems() {
        return presenter.getVisibleItems();
    }


    @Override
    public HandlerRegistration addCellPreviewHandler(
            com.google.gwt.view.client.CellPreviewEvent.Handler<T> handler) {

        return presenter.addCellPreviewHandler(handler);
    }
}
