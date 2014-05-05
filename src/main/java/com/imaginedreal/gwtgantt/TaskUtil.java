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
package com.imaginedreal.gwtgantt;

import com.imaginedreal.gwtgantt.model.Predecessor;
import com.imaginedreal.gwtgantt.model.Task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Brad Rydzewski
 */
public class TaskUtil {


    public static boolean moveUp(Task task, List<Task> tasks) {
        if(assertTasksExist(tasks)==false ||
                assertMoreThanOneTask(tasks)==false ||
                assertFirstTask(task, tasks)) {
            return false;
        }

        //get the prior task in the list
        Task priorTask = getPriorTask(task, tasks);

        //if no prior task was found, exit with value of false
        if(priorTask==null)
            return false;

        //get child tasks
        List<Task> taskChildren = getChildTasks(task, tasks);
        List<Task> priorTaskChildren = getChildTasks(priorTask, tasks);

        //swap the order of the tasks
        int taskOrder = task.getOrder();
        int priorTaskOrder = priorTask.getOrder();
        task.setOrder(priorTaskOrder);
        priorTask.setOrder(priorTaskOrder+taskChildren.size()+1);

        //fix the order of the child tasks
        repairTaskOrder(task, taskChildren);
        repairTaskOrder(priorTask, priorTaskChildren);

        //re-order
        Collections.sort(tasks, TASK_ORDER_COMPARATOR);

        return true;
    }

    public static boolean moveDown(Task task, List<Task> tasks) {
        if(assertTasksExist(tasks)==false ||
                assertMoreThanOneTask(tasks)==false ||
                assertLastTask(task, tasks)) {
            return false;
        }

        //get the new task in the list
        Task nextTask = getNextTask(task, tasks);

        //if no new task was found, exit with value of false
        if(nextTask==null)
            return false;

        //get child tasks
        List<Task> taskChildren = getChildTasks(task, tasks);
        List<Task> nextTaskChildren = getChildTasks(nextTask, tasks);

        //swap the order of the tasks
        int taskOrder = task.getOrder();
        int nextTaskOrder = nextTask.getOrder();
        task.setOrder(taskOrder+nextTaskChildren.size()+1);
        nextTask.setOrder(taskOrder);

        //fix the order of the child tasks
        repairTaskOrder(task, taskChildren);
        repairTaskOrder(nextTask, nextTaskChildren);

        //re-order
        Collections.sort(tasks, TASK_ORDER_COMPARATOR);

        return true;
    }

    public static boolean shiftLeft(Task task, List<Task> tasks) {
        if(assertTasksExist(tasks)==false ||
                assertMoreThanOneTask(tasks)==false ||
                assertFirstTask(task, tasks)) {
            return false;
        }

        //get the new task in the list
//        Task priorTask = getPriorTask(task, tasks);
//
//        //exit if prior task is null
//        if(priorTask==null)
//            return false;

        if(task.getLevel()>0) {

            List<Task> taskChildren = getChildTasks(task, tasks);
            task.setLevel(task.getLevel()-1);

            for(Task child : taskChildren) {
                child.setLevel(child.getLevel()-1);
            }

//            taskChildren = getChildTasks(task, tasks);
//            task.setSummary(taskChildren.size()>0);
//
//            if(priorTask!=null) {
//                taskChildren = getChildTasks(priorTask, tasks);
//                priorTask.setSummary(taskChildren.size()>0);
//            }

            repairTaskHierarchy(tasks);

            return true;
        }

        return false;
    }

    public static boolean shiftRight(Task task, List<Task> tasks) {
        if(assertTasksExist(tasks)==false ||
                assertMoreThanOneTask(tasks)==false ||
                assertFirstTask(task, tasks)) {
            return false;
        }

        //get the new task in the list
        Task priorTask = getPriorTask(task, tasks);

        //exit if prior task is null
        if(priorTask==null)
            return false;

        if(priorTask.getLevel()==task.getLevel()) {
            priorTask.setSummary(true);
            

            List<Task> taskChildren = getChildTasks(task, tasks);
            task.setLevel(task.getLevel()+1);
            
            for(Task child : taskChildren) {
                child.setLevel(child.getLevel()+1);
            }
            return true;
        }

        return false;
    }

    public static List<Task> delete(Task task, List<Task> tasks) {

        Map<Integer,Task> deleteTaskMap = new HashMap<Integer,Task>();
        //get the child tasks to be deleted
        List<Task> deleteTaskList = getChildTasks(task, tasks);

        //add the parent task to the delete list
        deleteTaskList.add(task);

        //add each task id to the hash map
        for(Task t : deleteTaskList) {
            deleteTaskMap.put(t.getUID(), t);
        }

        //remove the parent and child tasks
        tasks.removeAll(deleteTaskList);

        //iterate through and remove any predecessor references
        for(Task t : tasks) {
            //list of predecessors to remove
            List<Predecessor> deletePredList = new ArrayList<Predecessor>();
            //find predecessors that match the delete task
            for(Predecessor p : t.getPredecessors()) {
                //add to delete list if match found
                if(deleteTaskMap.containsKey(p.getUID())==true) {
                    deletePredList.add(p);
                }
            }
            //remove all task's predecessors from delete list
            t.getPredecessors().removeAll(deletePredList);
        }

        //re-do the hierarchy now that it is all f***ed up
        repairTaskHierarchy(tasks);
        
        return deleteTaskList;
    }

    public static boolean insertAbove(Task newTask, Task task, List<Task> tasks) {
        if(assertTasksExist(tasks)==false)
            return false;

        //get the max UID
        int maxUID = 1;
        for(Task t : tasks) {
            maxUID = Math.max(maxUID, t.getUID());
        }

        //increment the max by 1
        maxUID++;

        int taskOrder = tasks.size()+1;
        int taskLevel = (tasks.isEmpty())?0:tasks.get(tasks.size()-1).getLevel();
        Date taskStart = new Date();
        Date taskFinish = new Date();

        if(task!=null) {
        
            //increment the task orders
            int taskIndex = tasks.indexOf(task);
            taskOrder = task.getOrder();
            taskLevel = task.getLevel();
            taskStart = task.getStart();
            taskFinish = DateUtil.clone(taskStart);

            for(int i=taskIndex;i<tasks.size();i++) {
                Task t = tasks.get(i);
                t.setOrder(t.getOrder()+1);
            }
        }
        
        //set the task's data
        newTask.setUID(maxUID);
        newTask.setLevel(taskLevel);
        newTask.setOrder(taskOrder);
        newTask.setStart(taskStart);
        newTask.setFinish(taskFinish);


        //add the task to the list
        if(tasks.contains(newTask)==false)
            tasks.add(newTask);

        //re-order
        Collections.sort(tasks, TASK_ORDER_COMPARATOR);

        return true;
    }

    protected static Task getPriorTask(Task task, List<Task> tasks) {
        Task priorTask = null;
        int taskIndex = tasks.indexOf(task);

        //if the given task is the 1st in the list, exit
        if(assertFirstTask(task, tasks))
            return null;

        //if the prior task is a parent task, then exit
        if(tasks.get(taskIndex-1).getLevel()< task.getLevel())
            return null;


        do {
            taskIndex--;
            priorTask = tasks.get(taskIndex);
            if(priorTask.getLevel()==task.getLevel())
                return priorTask;
        } while (taskIndex>0);

        //nothing found, return null
        return null;
    }

    protected static Task getNextTask(Task task, List<Task> tasks) {
        Task nextTask = null;
        int taskIndex = tasks.indexOf(task);

        //if the given task is the 1st in the list, exit
        if(assertLastTask(task, tasks))
            return null;

        //if the prior task is a parent task, then exit
        if(tasks.get(taskIndex+1).getLevel()< task.getLevel())
            return null;


        do {
            taskIndex++;
            nextTask = tasks.get(taskIndex);
            if(nextTask.getLevel()==task.getLevel())
                return nextTask;
        } while (taskIndex<tasks.size());

        //nothing found, return null
        return null;
    }

    protected static List<Task> getChildTasks(Task task, List<Task> tasks) {
        List<Task> children = new ArrayList<Task>();
        Task childTask = null;
        int taskIndex = tasks.indexOf(task);

        //if this is the last task, it can't have any children
        if(assertLastTask(task, tasks))
            return children;

        //increment index
        taskIndex++;

        do {
            childTask = tasks.get(taskIndex);
            if(task.getLevel() < childTask.getLevel()) {
                children.add(childTask);
            } else break;
            taskIndex++;
        } while (taskIndex<tasks.size());

        return children;
    }

    protected static void repairTaskOrder(Task parent, List<Task> children) {
        int order = parent.getOrder();
//        System.out.println("repairing "+ parent.getUID()+ "  with order "+parent.getOrder());
        for(Task child : children) {
            order++;
//            System.out.println("   "+ child.getUID()+ "  with order "+order);
            child.setOrder(order);
        }
    }

    protected static void repairTaskHierarchy(List<Task> tasks) {
        for(int i=0;i<tasks.size();i++) {
            Task task = tasks.get(i);
            //check if task has children
            if(i<tasks.size()-1) {
                Task nextTask = tasks.get(i+1);
                task.setSummary(task.getLevel()<nextTask.getLevel());
            }

            task.setOrder(i+1);
        }
    }

    protected static boolean assertTasksExist(List<Task> tasks) {
        return tasks!=null && !tasks.isEmpty();
    }

    protected static boolean assertFirstTask(Task task, List<Task> tasks) {
        return tasks.get(0).equals(task);
    }

    protected static boolean assertLastTask(Task task, List<Task> tasks) {
        return tasks.get(tasks.size()-1).equals(task);
    }

    protected static boolean assertMoreThanOneTask(List<Task> tasks) {
        return tasks.size()>0;
    }

    public static final Comparator<Task> TASK_ORDER_COMPARATOR = new Comparator<Task>() {
        @Override
        public int compare(Task arg0, Task arg1) {
                return Integer.valueOf(arg0.getOrder()).compareTo(arg1.getOrder());
            }
	};
}
