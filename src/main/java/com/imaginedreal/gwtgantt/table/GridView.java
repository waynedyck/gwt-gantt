package com.imaginedreal.gwtgantt.table;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.dom.client.TableColElement;
import com.google.gwt.dom.client.TableElement;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.RowStyles;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.Range;
import com.google.gwt.view.client.RangeChangeEvent.Handler;
import com.google.gwt.view.client.SelectionModel;
import com.imaginedreal.gwtgantt.model.Task;
import com.imaginedreal.gwtgantt.table.override.Grid;

public class GridView<T> extends Composite implements HasData<T> {


	class CollapsibleRowStyle implements RowStyles<Task> {
		private boolean collapse = false;
		private int collapseLevel = -1;

		public CollapsibleRowStyle() {

		}

		public void reset() {
			collapseLevel = -1;
			collapse = false;
		}

		@Override
		public String getStyleNames(Task row, int rowIndex) {

			if(rowIndex==0) {
				reset();
			}

			String style = null;
			collapse = collapse && row.getLevel()>=collapseLevel;

			if(!collapse) {
				if(row.isSummary()) {
					collapse = row.isCollapsed();
					collapseLevel = row.getLevel()+1;
					style = "cellTableSummaryRow";
				}

			} else {
				style = "cellTableRowIsCollapsed";
			}
			return style;
		}
	}

	class HeaderTable extends Grid {
		
		public HeaderTable(int rows, int cols) {
			super(rows, cols);
			this.sinkEvents(Event.ONMOUSEMOVE | Event.ONMOUSEDOWN
			        | Event.ONMOUSEUP | Event.ONCLICK | Event.ONMOUSEOUT);
		}
		
		@Override
		  public void onBrowserEvent(Event event) {
		    super.onBrowserEvent(event);
		    switch (DOM.eventGetType(event)) {
		      case Event.ONMOUSEDOWN:
		        // Start resizing a header column
		        if (DOM.eventGetButton(event) != Event.BUTTON_LEFT) {
		          return;
		        }
		        if (resizeWorker.getCurrentCell() != null) {
		          DOM.eventPreventDefault(event);
		          DOM.eventCancelBubble(event, true);
		          resizeWorker.startResizing(event);
		        }
		        break;
		      case Event.ONMOUSEUP:
		        if (DOM.eventGetButton(event) != Event.BUTTON_LEFT) {
		          return;
		        }
		        // Stop resizing the header column
		        if (resizeWorker.isResizing()) {
		          resizeWorker.stopResizing(event);
		        }
		        break;
		      case Event.ONMOUSEMOVE:
		        // Resize the header column
		        if (resizeWorker.isResizing()) {
		          resizeWorker.resizeColumn(event);
		        } else {
		          resizeWorker.setCurrentCell(event);
		        }
		        break;
		      case Event.ONMOUSEOUT:
		        break;
		    }
		  }
	}

	class ColumnResizeWorker {

	    /**
	     * The current header cell that the mouse is affecting.
	     */
	    private Element curCell = null;

	    /**
	     * The index of the current header cell.
	     */
	    private int curCellIndex = 0;

	    /**
	     * The current x position of the mouse.
	     */
	    private int mouseXCurrent = 0;

	    /**
	     * The last x position of the mouse when we resized.
	     */
	    private int mouseXLast = 0;

	    /**
	     * The starting x position of the mouse when resizing a column.
	     */
	    private int mouseXStart = 0;

	    /**
	     * A timer used to resize the columns. As long as the timer is active, it
	     * will poll for the new row size and resize the columns.
	     */
	    private Timer resizeTimer = new Timer() {
	      @Override
	      public void run() {
	        resizeColumn();
	        schedule(100);
	      }
	    };

	    /**
	     * A boolean indicating that we are resizing the
	     * current header cell.
	     */
	    private boolean resizing = false;

	    /**
	     * Returns the current cell.
	     * 
	     * @return the current cell
	     */
	    public Element getCurrentCell() {
	      return curCell;
	    }

	    /**
	     * Returns true if a header is currently being resized.
	     * 
	     * @return true if resizing
	     */
	    public boolean isResizing() {
	      return resizing;
	    }

	    /**
	     * Resize the column on a mouse event. This method also marks the client as
	     * busy so we do not try to change the size repeatedly.
	     * 
	     * @param event the mouse event
	     */
	    public void resizeColumn(Event event) {
	      mouseXCurrent = DOM.eventGetClientX(event);
	    }

	    /**
	     * Set the current cell that will be resized based on the mouse event.
	     * 
	     * @param event the event that triggered the new cell
	     * @return true if the cell was actually changed
	     */
	    public boolean setCurrentCell(Event event) {
	      // Check the resize policy of the table
	      Element cell = null;
	      cell = headerTable.getEventTargetCell(event);
	      

	      // See if we are near the edge of the cell
	      int clientX = DOM.eventGetClientX(event);
	      if (cell != null) {
	        int absRight = DOM.getAbsoluteLeft(cell)
	            + DOM.getElementPropertyInt(cell, "offsetWidth");
	        if (clientX < absRight - 15 || clientX > absRight) {
	          cell = null;
	        }
	      }

	      // Change out the current cell
	      if (cell != curCell) {
	        // Clear the old cell
	        if (curCell != null) {
	          DOM.setStyleAttribute(curCell, "cursor", "");
	        }

	        // Set the new cell
	        curCell = cell;
	        if (curCell != null) {
	          curCellIndex = getCellIndex(curCell);
	          if (curCellIndex < 1) { //set to 1, since column 0 is not draggable
	            curCell = null;
	            return false;
	          }
	          DOM.setStyleAttribute(curCell, "cursor", "e-resize");
	        }
	        return true;
	      }

	      // The cell did not change
	      return false;
	    }

	    /**
	     * Start resizing the current cell when the user clicks on the right edge of
	     * the cell.
	     * 
	     * @param event the mouse event
	     */
	    public void startResizing(Event event) {
	      if (curCell != null) {
	        resizing = true;
	        mouseXStart = DOM.eventGetClientX(event);
	        mouseXLast = mouseXStart;
	        mouseXCurrent = mouseXStart;
	        // Start the timer and listen for changes
	        DOM.setCapture(headerTable.getElement());//TODO: may want to set to header panel instead of table
	        resizeTimer.schedule(5);//20
	      }
	    }

	    /**
	     * Stop resizing the current cell.
	     * 
	     * @param event the mouse event
	     */
	    public void stopResizing(Event event) {
	      if (curCell != null && resizing) {
	        resizing = false;
	        DOM.releaseCapture(headerTable.getElement()); //TODO: may need to use header panel here
	        resizeTimer.cancel();
	        resizeColumn();
	      }
	    }

	    /**
	     * Get the actual cell index of a cell in the header table.
	     * 
	     * @param cell the cell element
	     * @return the cell index
	     */
	    private int getCellIndex(Element cell) {
	    	return DOM.getChildIndex(DOM.getParent(cell), cell);
	    }

	    /**
	     * Helper method that actually sets the column size. This method is called
	     * periodically by a timer.
	     */
	    private void resizeColumn() {
	    	
	      if (mouseXLast != mouseXCurrent) {
	        
	        int currWidth = curCell.getClientWidth()-1;
	        int newWidth = currWidth + (mouseXCurrent-mouseXLast);
	        GridView.this.resizeColumn(curCellIndex, newWidth);
	        mouseXLast = mouseXCurrent;
	      }
	    }
	}
	
	class TaskGridNameCellImpl extends TaskGridNameCell {
		@Override
		public void onExpandCollapse(Task task) {
			taskTable.redraw();

		}
	}

    class BodyPanel extends SimplePanel {
            public BodyPanel() {
                sinkEvents(Event.ONSCROLL);
            }
            
            @Override
        	protected void onAttach() {
        		super.onAttach();
        		headerTable.getElement().getStyle().setLeft(0, Unit.PX);
        		taskTable.getElement().getStyle().setLeft(0, Unit.PX);
        	}
            
            @Override
            public void onBrowserEvent(Event event) {
                switch (DOM.eventGetType(event)) {
                    case Event.ONSCROLL:

			int left = DOM.getElementPropertyInt(getElement(), "scrollLeft");
			headerTable.getElement().getStyle().setLeft(left*-1, Unit.PX);
                        break;
                }
                super.onBrowserEvent(event);
            }
        }


    
//	private String[] columnNameArray = new String[]{"&nbsp;","Task Name","Duration","Start","Finish","Predecessors","Resources"};
//	private int[] columnWidthArray = new int[]{50, 350,80,110,110,100, 100};
//	private TableColElement[] columnGroupArray = null;
	private FlowPanel container = new FlowPanel();
	private BodyPanel body = new BodyPanel();
	private FlowPanel header = new FlowPanel();
	private HeaderTable headerTable = new HeaderTable(1,1);
	private CellTable<T> taskTable = null;
	private RowStyles<T> rowStyle = (RowStyles<T>) new CollapsibleRowStyle();
	private ColumnResizeWorker resizeWorker = new ColumnResizeWorker();
	private TableElement tableElement = null;
	
	private List<TaskGridColumn> columns = new ArrayList<TaskGridColumn>();

	public GridView() {
		initWidget(container);
		
		GridViewResources.INSTANCE.style().ensureInjected();
		CellTableResources.INSTANCE.cellTableStyle().ensureInjected();
		
		taskTable = new CellTable<T>(Integer.MAX_VALUE,CellTableResources.INSTANCE);
//		taskTable.setSelectionModel(selectionModel);
		taskTable.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.DISABLED);
		tableElement = taskTable.getElement().cast();
		
		container.setStyleName(GridViewResources.INSTANCE.style().taskGrid());
		header.setStyleName(GridViewResources.INSTANCE.style().header());
		headerTable.setCellPadding(0);
		headerTable.setCellSpacing(0);
		header.add(headerTable);

		body.setStyleName(GridViewResources.INSTANCE.style().body());
		body.add(taskTable);
		
		container.add(header);
		container.add(body);
		
//		initTableColumns();
		
		taskTable.setRowStyles(rowStyle);
		
	}

	public void setRowStyles(RowStyles<T> styles) {
        taskTable.setRowStyles(styles);
    }
	
	public void addColumns(List<TaskGridColumn> columns) {
		this.columns.clear();
		this.columns.addAll(columns);
		headerTable.clear();
		headerTable.resizeColumns(columns.size());
//		columnGroupArray = new TableColElement[columns.size()];
		
		String shortFormatPattern = "MM/dd/yy";//DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT).getPattern();
		String dayFormatPattern = "EEE";
		DateTimeFormat format = DateTimeFormat.getFormat(
				dayFormatPattern + " " + shortFormatPattern);
		
		//set width of each column
		for(int i=0;i<columns.size();i++) {
			
//			TableColElement colgroup = Document.get().createColElement();
//			colgroup.setWidth(columns.get(i).getWidth()+"px");
//			columnGroupArray[i] = colgroup;
			TaskGridColumn column = columns.get(i);
			headerTable.setHTML(0, i, "<div>"+column.getName()+"</div>");
			headerTable.getCellFormatter().setWidth(0, i, column.getWidth()+"px");
			headerTable.getCellFormatter().setVerticalAlignment(0, i, HasVerticalAlignment.ALIGN_TOP);
			taskTable.addColumn(column.getColumn());
			taskTable.addColumnStyleName(i, (column.getStyle()==null)?"no-header":column.getStyle());

		}
		
		for(int i=0;i<columns.size();i++) {
			headerTable.getElement().getFirstChild().getChild(i).<TableColElement>cast().setWidth(columns.get(i).getWidth()+"px");
			tableElement.getElementsByTagName("colgroup").getItem(0).getChild(i).<TableColElement>cast().setWidth(columns.get(i).getWidth()+"px");
		}
		
//		//add each column group to the table
//		for(int i=columns.size()-1;i>=0;i--) {
//			tableElement.getTFoot()
//			taskTable.getElement().getFirstChildElement().get
//		}
		
//
//		//ORDER COLUMN
//		Column<Task, String> orderColumn = new Column<Task, String>(new TextCell()) {
//
//			@Override
//			public String getValue(Task object) {
//				return String.valueOf(object.getOrder());
//			}
//		};
//		
//		//NAME COLUMN
//		Column<Task, Task> nameColumn = new Column<Task, Task>(new TaskGridNameCellImpl()) {
//
//			@Override
//			public Task getValue(Task object) {
//				return object;
//			}
//		};
//		nameColumn.setFieldUpdater(new FieldUpdater<Task, Task>() {
//			public void update(int index, Task object, Task value) {
//				// Called when the user changes the value.
//				//object.setName(value);
//			}
//		});
//				
//		//DURATION COLUMN
//		Column<Task, String> durationColumn = new Column<Task, String>(new EditTextCell()) {
//
//			@Override
//			public String getValue(Task object) {
//				return String.valueOf(object.getDuration());
//			}
//		};
//		durationColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
//			public void update(int index, Task object, String value) {
//				// Called when the user changes the value.
////				object.setper
//			}
//		});
//		
//		//START COLUMN
//		Column<Task, Date> startColumn = new Column<Task, Date>(new DateCell(format)) {
//			@Override
//			public Date getValue(Task object) {
//				return object.getStart();
//			}
//		};
//		startColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//			public void update(int index, Task object, Date value) {
//				object.setStart(value);
//			}
//		});
//		
//		//FINISH COLUMN
//		Column<Task, Date> finishColumn = new Column<Task, Date>(new DateCell(format)) {
//			@Override
//			public Date getValue(Task object) {
//				return object.getFinish();
//			}
//		};
//		finishColumn.setFieldUpdater(new FieldUpdater<Task, Date>() {
//			public void update(int index, Task object, Date value) {
//				object.setFinish(value);
//			}
//		});
//		
//		//PREDECESSOR COLUMN
//	    Column<Task, String> predecessorColumn = new Column<Task, String>(new EditTextCell()) {
//	    	@Override
//	    	public String getValue(Task object) {
//	    		return "";
//	    	}
//	    };
//	    predecessorColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
//	    	public void update(int index, Task object, String value) {
//	    		
//	    	}
//	    });
//		
//		//RESOURCE COLUMN
//	    Column<Task, String> resourceColumn = new Column<Task, String>(new EditTextCell()) {
//	    	@Override
//	    	public String getValue(Task object) {
//	    		return "";
//	    	}
//	    };
//	    resourceColumn.setFieldUpdater(new FieldUpdater<Task, String>() {
//	    	public void update(int index, Task object, String value) {
//	    		
//	    	}
//	    });
//	    
//		taskTable.addColumn(orderColumn);
//		taskTable.addColumn(nameColumn);
//		taskTable.addColumn(durationColumn);
//		taskTable.addColumn(startColumn);
//		taskTable.addColumn(finishColumn);
//		taskTable.addColumn(predecessorColumn);
//		taskTable.addColumn(resourceColumn);
	}
	
	void resizeColumn(int columnIndex, int width) {
		
		if(columnIndex==0)
			return;
		
		//store the width
		columns.get(columnIndex).setWidth(width);
		//update the table
//		headerTable.getCellFormatter().setWidth(
//				0, columnIndex, width+"px");
//		bodyTable.getCellFormatter().setWidth(
//				0, columnIndex, width+"px");
		
		headerTable.getElement().getFirstChild().getChild(columnIndex).<TableColElement>cast().setWidth(columns.get(columnIndex).getWidth()+"px");
		tableElement.getElementsByTagName("colgroup").getItem(0).getChild(columnIndex).<TableColElement>cast().setWidth(columns.get(columnIndex).getWidth()+"px");
		
//		headerTable.getElement().getFirstChild().getChild(columnIndex).<TableColElement>cast().setWidth(columns.get(columnIndex).getWidth()+"px");
//		tableElement.getFirstChild().getChild(columnIndex).<TableColElement>cast().setWidth(columns.get(columnIndex).getWidth()+"px");
//		columnGroupArray[columnIndex].getStyle().setProperty("width",width, Unit.PX);
	}

	
	
	public void enableVerticalScrolling(boolean enabled) {
            body.getElement().getStyle().setProperty("overflowY", (enabled)?"scroll":"hidden");
        }
	
	public void enableHorizontalScrolling(boolean enabled) {
           
            body.getElement().getStyle().setProperty("overflowX", (enabled)?"scroll":"hidden");
        }
//	public void setHorizontalScrollPosition(int position) {
//            DOM.setElementPropertyInt(body.getElement(), "scrollLeft", position);
//        }
	public void setVerticalScrollPosition(int position) {
//            DOM.setElementPropertyInt(body.getElement(), "scrollTop", position);
            taskTable.getElement().getStyle().setTop(position*-1, Unit.PX);
        }
	

	
	@Override
	public HandlerRegistration addRangeChangeHandler(Handler handler) {
		return taskTable.addRangeChangeHandler(handler);
	}

	@Override
	public HandlerRegistration addRowCountChangeHandler(
			com.google.gwt.view.client.RowCountChangeEvent.Handler handler) {
		return taskTable.addRowCountChangeHandler(handler);
	}

	@Override
	public int getRowCount() {
		return taskTable.getRowCount();
	}

	@Override
	public Range getVisibleRange() {
		return taskTable.getVisibleRange();
	}

	@Override
	public boolean isRowCountExact() {
		return taskTable.isRowCountExact();
	}

	@Override
	public void setRowCount(int count) {
		taskTable.setRowCount(count);
	}

	@Override
	public void setRowCount(int count, boolean isExact) {
		taskTable.setRowCount(count, isExact);
	}

	@Override
	public void setVisibleRange(int start, int length) {
		taskTable.setVisibleRange(start, length);
	}

	@Override
	public void setVisibleRange(Range range) {
		taskTable.setVisibleRange(range);
	}

	@Override
	public SelectionModel<? super T> getSelectionModel() {
		return taskTable.getSelectionModel();
	}

	@Override
	public void setRowData(int start, List<? extends T> values) {
		taskTable.setRowData(start, values);
	}

	@Override
	public void setSelectionModel(SelectionModel<? super T> selectionModel) {
		taskTable.setSelectionModel(selectionModel);
	}

	@Override
	public void setVisibleRangeAndClearData(Range range,
			boolean forceRangeChangeEvent) {
		taskTable.setVisibleRangeAndClearData(range, forceRangeChangeEvent);
	}
	
	public void redraw() {
		taskTable.redraw();
	}

    @Override
    public HandlerRegistration addCellPreviewHandler(
            com.google.gwt.view.client.CellPreviewEvent.Handler<T> handler) {

        return taskTable.addCellPreviewHandler(handler);
    }

    @Override
    public T getVisibleItem(int indexOnPage) {

        return taskTable.getVisibleItem(indexOnPage);
    }

    @Override
    public int getVisibleItemCount() {

        return taskTable.getVisibleItemCount();
    }

    @Override
    public Iterable<T> getVisibleItems() {

        return taskTable.getVisibleItems();
    }
}
