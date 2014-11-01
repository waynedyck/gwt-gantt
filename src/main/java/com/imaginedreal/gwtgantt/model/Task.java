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
package com.imaginedreal.gwtgantt.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Defines the behaviors of a <code>Task</code> to be displayed
 * in the <code>GanttChart</code> widget.
 *
 * @author Brad Rydzewski
 */
public class Task implements Serializable {

    private static final long serialVersionUID = 7961968545635865703L;
    public static final String STYLE_BLUE = "blue";
    public static final String STYLE_RED = "red";
    public static final String STYLE_GREEN = "green";
    public static final String STYLE_ORANGE = "orange";
    public static final String STYLE_PURPLE = "purple";
    public static final String STYLE_DEFAULT = STYLE_BLUE;
    private int UID;
    private String name;
    private String notes;
    private int order;
    private int level;
    private Date start;
    private Date finish;
    private double duration;
    private DurationFormat durationFormat = DurationFormat.DAYS;
    private int percentComplete;
    private boolean milestone;
    private boolean summary;
    private boolean collapsed;
    private String style = STYLE_DEFAULT;
    private List<Predecessor> predecessors;

    public Task() {
        predecessors = new ArrayList<Predecessor>();
    }


    /**
     * Gets the unique ID of this task. For internal use only to compare and
     * link two tasks.
     *
     * @return A numeric ID identifying each task managed by gwt-gantt
     */
    public int getUID() {
        return UID;
    }

    /**
     * Gets the Name of the task.
     *
     * @return Task name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the Text notes associated with the task.
     *
     * @return Task notes.
     */
    public String getNotes() {
        return notes;
    }

    /**
     * Gets the Order of the task.
     *
     * @return Task order.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Sets the Order of the task.
     *
     * @param order
     *            Task order.
     */
    public void setOrder(int order) {
        this.order = order;
    }

    /**
     * Gets the indent Level of the task.
     *
     * @return Task indentation level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the indent Level of the task.
     *
     * @param level
     *            Task indentation level.
     */
    public void setLevel(int level) {
        this.level = level;
    }

    /**
     * Gets the start Date of the task.
     *
     * @return Task start date.
     */
    public Date getStart() {
        return start;
    }

    /**
     * Sets the start Date of the task.
     *
     * @param start
     *            Task start date.
     */
    public void setStart(Date start) {
        this.start = start;
    }

    /**
     * Gets the finish Date of the task.
     *
     * @return Task finish date.
     */
    public Date getFinish() {
        return finish;
    }

    /**
     * Sets the finish Date of the task.
     *
     * @param finish
     *            Task finish date.
     */
    public void setFinish(Date finish) {
        this.finish = finish;
    }

    /**
     * Whether the task is a milestone.
     *
     * @return True or False if the task is a milestone.
     */
    public boolean isMilestone() {
        return milestone;
    }

    /**
     * Whether the task is a summary.
     *
     * @return True or False if the task is a summary.
     */
    public boolean isSummary() {
        return summary;
    }

    /**
     * Whether the task is Hidden and should not be displayed on the Gantt
     * chart. This is used for UI rendering only, and has no other use
     * or meaning.
     *
     * @return True or False if the task is hidden.
     */
    public boolean isCollapsed() {
        return collapsed;
    }

    /**
     * Gets the List of {@link Predecessor}s for this task.
     *
     * @return
     */
    public List<Predecessor> getPredecessors() {
        return predecessors;
    }

    /**
     * Sets the unique ID of this task.
     * @param UID
     */
    public void setUID(int UID) {
        this.UID = UID;
    }

    public void setMilestone(boolean milestone) {
        this.milestone = milestone;
    }

    /**
     * Sets whether the task is a summary (i.e. has child tasks).
     * @param summary True or False if the task is a summary
     */
    public void setSummary(boolean summary) {
        this.summary = summary;
    }

    /**
     * Sets whether or not the Task should be visually collapsed,
     * hiding all of its child tasks on the Gantt chart.
     * @param collapsed Flag indicating if task should be collapsed
     */
    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    /**
     * Sets the Name of the task.
     *
     * @param name Task name.
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setPredecessors(List<Predecessor> predecessors) {
        this.predecessors = predecessors;
    }

    /**
     * Sets the CSS Style used when rendering this Task.
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * Gets the CSS Style used when rendering this Task.
     */
    public String getStyle() {
        return style;
    }

    /**
     * Set the percentage of the task duration completed, as a whole number
     * between 0 and 100.
     * @param percentComplete Percent complete.
     */
    public void setPercentComplete(int percentComplete) {
        this.percentComplete = percentComplete;
    }

    /**
     * The percentage of the task duration completed, as a whole number
     * between 0 and 100.
     *
     * @return Percent complete.
     */
    public int getPercentComplete() {
        return percentComplete;
    }

    /**
     * Gets the duration of the Task.
     * @return Task duration.
     */
    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Gets the unit of measure for expressing a duration of time.
     * @return Task duration format.
     */
    public DurationFormat getDurationFormat() {
        return durationFormat;
    }

    public void setDurationFormat(DurationFormat durationFormat) {
        this.durationFormat = durationFormat;
    }

    public boolean addPredecessor(int UID) {
        return addPredecessor(UID, PredecessorType.FS);
    }

    public boolean addPredecessor(int UID, PredecessorType type) {
        return predecessors.add((Predecessor) new Predecessor(UID, type));
    }


    public int compareTo(Task o) {
        return Integer.valueOf(order).compareTo(o.getOrder());
    }
    
    public boolean equals(Object o) {
        return o instanceof Task && 
               o != null &&
               ((Task)o).getUID() == this.UID;
    }

    public Task clone() { //throws CloneNotSupportedException {
        Task task = new Task();
        task.UID = this.UID;
        task.finish = this.finish;
        task.level = this.level;
        task.milestone = this.milestone;
        task.summary = this.summary;
        task.name = this.name;
        task.notes = this.notes;
        task.order = this.order;
        task.predecessors = this.predecessors;
        task.start = this.start;
        task.style = this.style;
        task.durationFormat = this.durationFormat;
        task.duration = this.duration;
        task.percentComplete = this.percentComplete;

        return task;
    }
}
