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

import com.imaginedreal.gwtgantt.model.DurationFormat;
import com.imaginedreal.gwtgantt.model.Predecessor;
import com.imaginedreal.gwtgantt.model.PredecessorType;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * Implementors of {@link ProvidesTask} provide data fields for
 * Tasks, rendered by the {@link TaskDisplay} component.
 * </p>
 *
 * @param <T> the data type of items to provide
 */
public interface ProvidesTask<T> {

    /**
     * Gets the unique ID of this task. For internal use only to compare and
     * link two tasks.
     *
     * @return A numeric ID identifying each task managed by gwt-gantt
     */
    public int getUID(T item);

    /**
     * Gets the Name of the task.
     *
     * @return Task name.
     */
    public String getName(T item);

    /**
     * Gets the Text notes associated with the task.
     *
     * @return Task notes.
     */
    public String getNotes(T item);

    /**
     * Gets the Order of the task.
     *
     * @return Task order.
     */
    public int getOrder(T item);

    /**
     * Gets the indent Level of the task.
     *
     * @return Task indentation level.
     */
    public int getLevel(T item);

    /**
     * Gets the start Date of the task.
     *
     * @return Task start date.
     */
    public Date getStart(T item);

    /**
     * Gets the finish Date of the task.
     *
     * @return Task finish date.
     */
    public Date getFinish(T item);

    /**
     * Gets the duration of the Task.
     * @return Task duration.
     */
    public double getDuration(T item);

    /**
     * Gets the unit of measure for expressing a duration of time.
     * @return Task duration format.
     */
    public DurationFormat getDurationFormat(T item);

    /**
     * The percentage of the task duration completed, as a whole number
     * between 0 and 100.
     * @return Percent complete.
     */
    public int getPercentComplete(T item);

    /**
     * Whether the task is a milestone.
     *
     * @return True or False if the task is a milestone.
     */
    public boolean isMilestone(T item);

    /**
     * Whether the task is a summary.
     *
     * @return True or False if the task is a summary.
     */
    public boolean isSummary(T item);

    /**
     * Whether the task is Hidden and should not be displayed on the Gantt
     * chart. This is used for UI rendering only, and has no other use
     * or meaning.
     *
     * @return True or False if the task is hidden.
     */
    public boolean isCollapsed(T item);

    /**
     * Gets the CSS Style used when rendering this Task.
     */
    public String getStyle(T item);

    /**
     * Gets the List of {@link Predecessor}s for this task.
     *
     * @return
     */
    public List<Predecessor> getPredecessors(T item);

    /**
     * Gets the count of Predecessors for this task.
     * @param item
     * @return
     */
    public int getPredecessorCount(T item);

    /**
     * Gets the Predecessor Task UID for the given predecessor index.
     * @param item
     * @param index
     * @return
     */
    public int getPredecessorUID(T item, int index);

    /**
     * Gets the {@link PredecessorType} for the given predecessor index.
     * @param item
     * @param index
     * @return
     */
    public PredecessorType getPredecessorType(T item, int index);
}
