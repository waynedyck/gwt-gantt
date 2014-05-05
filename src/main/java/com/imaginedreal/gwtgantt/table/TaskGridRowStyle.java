/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.imaginedreal.gwtgantt.table;

import com.google.gwt.user.cellview.client.RowStyles;
import com.imaginedreal.gwtgantt.gantt.ProvidesTask;

/**
 *
 * @author Brad Rydzewski
 */
public class TaskGridRowStyle<T> implements RowStyles<T> {

    private boolean collapse = false;
    private int collapseLevel = -1;
    private ProvidesTask<T> provider;

    public TaskGridRowStyle(ProvidesTask<T> provider) {
        this.provider = provider;
    }

    public void reset() {
        collapseLevel = -1;
        collapse = false;
    }

    @Override
    public String getStyleNames(T row, int rowIndex) {

        if (rowIndex == 0) {
            reset();
        }

        String style = null;
        collapse = collapse && provider.getLevel(row) >= collapseLevel;

        if (!collapse) {
            if (provider.isSummary(row)) {
                collapse = provider.isCollapsed(row);
                collapseLevel = provider.getLevel(row) + 1;
                style = "cellTableSummaryRow";
            }

        } else {
            style = "cellTableRowIsCollapsed";
        }
        return style;
    }


}
