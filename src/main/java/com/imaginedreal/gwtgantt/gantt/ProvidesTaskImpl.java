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
import com.imaginedreal.gwtgantt.model.Task;

import java.util.Date;
import java.util.List;

/**
 *
 * @author Brad Rydzewski
 */
public class ProvidesTaskImpl implements ProvidesTask<Task> {

    @Override
    public int getUID(Task item) {
        return item.getUID();
    }

    @Override
    public String getName(Task item) {
        return item.getName();
    }

    @Override
    public String getNotes(Task item) {
        return item.getNotes();
    }

    @Override
    public int getOrder(Task item) {
        return item.getOrder();
    }

    @Override
    public int getLevel(Task item) {
        return item.getLevel();
    }

    @Override
    public Date getStart(Task item) {
        return item.getStart();
    }

    @Override
    public Date getFinish(Task item) {
        return item.getFinish();
    }

    @Override
    public double getDuration(Task item) {
        return item.getDuration();
    }

    @Override
    public DurationFormat getDurationFormat(Task item) {
        return item.getDurationFormat();
    }

    @Override
    public int getPercentComplete(Task item) {
        return item.getPercentComplete();
    }

    @Override
    public boolean isMilestone(Task item) {
        return item.isMilestone();
    }

    @Override
    public boolean isSummary(Task item) {
        return item.isSummary();
    }

    @Override
    public boolean isCollapsed(Task item) {
        return item.isCollapsed();
    }

    @Override
    public String getStyle(Task item) {
        return item.getStyle();
    }

    @Override
    public List<Predecessor> getPredecessors(Task item) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int getPredecessorCount(Task item) {
        return item.getPredecessors().size();
    }

    @Override
    public int getPredecessorUID(Task item, int index) {
        return item.getPredecessors().get(index).getUID();
    }

    @Override
    public PredecessorType getPredecessorType(Task item, int index) {
        return item.getPredecessors().get(index).getType();
    }

}
