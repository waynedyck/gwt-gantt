package com.imaginedreal.gwtgantt;

import com.imaginedreal.gwtgantt.TaskUtil;
import com.imaginedreal.gwtgantt.model.Task;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Brad Rydzewski
 */
public class TaskUtilTest {

    List<Task> getMockTaskList() {
        List<Task> taskList = new ArrayList<Task>();

        //1.0
        Task task1 = new Task();
        task1.setUID(1);
        task1.setName("Task 1");
        task1.setOrder(1);
        task1.setLevel(1);
        taskList.add(task1);
        //2.0
        Task task2 = new Task();
        task2.setUID(2);
        task2.setName("Task 2");
        task2.setOrder(2);
        task2.setLevel(1);
        task2.setSummary(true);
        taskList.add(task2);
        //2.1
        Task task3 = new Task();
        task3.setUID(3);
        task3.setName("Task 3");
        task3.setOrder(3);
        task3.setLevel(2);
        taskList.add(task3);
        //2.2
        Task task4 = new Task();
        task4.setUID(4);
        task4.setName("Task 4");
        task4.setOrder(4);
        task4.setLevel(2);
        taskList.add(task4);
        //3.0
        Task task5 = new Task();
        task5.setUID(5);
        task5.setName("Task 5");
        task5.setOrder(5);
        task5.setLevel(1);
        taskList.add(task5);

        return taskList;
    }

    @Test
    public void getPriorTaskTest_FirstTask() {
        List<Task> taskList = getMockTaskList();
        Task firstTask = taskList.get(0);
        Task priorTask = TaskUtil.getPriorTask(firstTask, taskList);
        assertNull("First Task in the list should not have any prior tasks",priorTask);
    }

    @Test
    public void getPriorTaskTest_FirstChild() {
        List<Task> taskList = getMockTaskList();
        Task firstChild = taskList.get(2);
        Task priorTask = TaskUtil.getPriorTask(firstChild, taskList);
        assertNull("First child Task in the list should not have any prior tasks",priorTask);
    }

    @Test
    public void getPriorTaskTest_PrevTaskAtSameLevel() {
        List<Task> taskList = getMockTaskList();
        Task firstChild = taskList.get(3);
        Task priorTask = TaskUtil.getPriorTask(firstChild, taskList);
        assertNotNull("Prior task should not be null",priorTask);
        assertTrue("Prior task should have UID of 3",priorTask.getUID()==3);
    }

    @Test
    public void getPriorTaskTest_TaskHasChildren() {
        List<Task> taskList = getMockTaskList();
        Task firstChild = taskList.get(4);
        Task priorTask = TaskUtil.getPriorTask(firstChild, taskList);
        assertNotNull("Prior task should not be null",priorTask);
        assertTrue("Prior task should have UID of 2",priorTask.getUID()==2);
    }

    @Test
    public void getNextTaskTest_LastTask() {
        List<Task> taskList = getMockTaskList();
        Task lastTask = taskList.get(4);
        Task nextTask = TaskUtil.getNextTask(lastTask, taskList);
        assertNull("Last Task in the list should not have a 'next' task",nextTask);
    }

    @Test
    public void getNextTaskTest_LastChild() {
        List<Task> taskList = getMockTaskList();
        Task lastTask = taskList.get(3);
        Task nextTask = TaskUtil.getNextTask(lastTask, taskList);
        assertNull("Last child Task in the list should not have a 'next' task",nextTask);
    }

    @Test
    public void getNextTaskTest_NextTaskAtSameLevel() {
        List<Task> taskList = getMockTaskList();
        Task firstChild = taskList.get(2);
        Task nextTask = TaskUtil.getNextTask(firstChild, taskList);
        assertNotNull("Next task should not be null",nextTask);
        assertTrue("Next task should have UID of 4",nextTask.getUID()==4);
    }

    @Test
    public void getNextTaskTest_TaskHasChildren() {
        List<Task> taskList = getMockTaskList();
        Task firstChild = taskList.get(1);
        Task nextTask = TaskUtil.getNextTask(firstChild, taskList);
        assertNotNull("Next task should not be null",nextTask);
        assertTrue("Next task should have UID of 5",nextTask.getUID()==5);
    }

    @Test
    public void getChildTasksTest_TaskHasChildren() {
        List<Task> taskList = getMockTaskList();
        Task task = taskList.get(1);
        List<Task> childTaskList = TaskUtil.getChildTasks(task, taskList);
        assertTrue("Task has 2 children",childTaskList.size()==2);
    }

    @Test
    public void getChildTasksTest_TaskHasNoChildren() {
        List<Task> taskList = getMockTaskList();
        Task task = taskList.get(0);
        List<Task> childTaskList = TaskUtil.getChildTasks(task, taskList);
        assertTrue("Task has no children",childTaskList.isEmpty());
    }

    @Test
    public void getChildTasksTest_LastTask() {
        List<Task> taskList = getMockTaskList();
        Task task = taskList.get(4);
        List<Task> childTaskList = TaskUtil.getChildTasks(task, taskList);
        assertTrue("Last Task in list cannot have children",childTaskList.isEmpty());
    }

    @Test
    public void testMoveUp_NoChildren() {
        List<Task> taskList = getMockTaskList();
        Task taskToMove = taskList.get(3);
        boolean moved = TaskUtil.moveUp(taskToMove, taskList);
        assertTrue("Task moved == true", moved);
        assertTrue("Task order changed from 4 to 3", taskToMove.getOrder()==3);
        assertTrue("Task has index of 2 in list", taskList.indexOf(taskToMove)==2);
    }

    @Test
    public void testMoveUp_HasChildren() {
        List<Task> taskList = getMockTaskList();
        Task taskToMove = taskList.get(1);
        boolean moved = TaskUtil.moveUp(taskToMove, taskList);
        assertTrue("Task moved == true", moved);
//        assertTrue("Task order changed from 2 to 1", taskToMove.getOrder()==1);
//        assertTrue("Task has index of 0 in list", taskList.indexOf(taskToMove)==0);

        assertTrue(taskList.get(0).getUID()==2);
        assertTrue(taskList.get(1).getUID()==3);
        assertTrue(taskList.get(2).getUID()==4);
        assertTrue(taskList.get(3).getUID()==1);
    }

    @Test
    public void testShiftRight_HasChildren() {
        List<Task> taskList = getMockTaskList();
        Task taskToShift = taskList.get(3);
        boolean shifted = TaskUtil.shiftRight(taskToShift, taskList);
        assertTrue("Task shifted == true", shifted);

        assertTrue("Prior task remains at level 2",taskList.get(2).getLevel()==2);
        assertTrue("Prior task set to summary",taskList.get(2).isSummary()==true);
        assertTrue("Task indented to level 3",taskList.get(3).getLevel()==3);
    }

    @Test
    public void testDelete_NoChildren() {
        List<Task> taskList = getMockTaskList();
        Task taskToDelete = taskList.get(3);
        TaskUtil.delete(taskToDelete, taskList);
        assertTrue("Task removed from list",taskList.contains(taskToDelete)==false);
        assertTrue("Task order updated after delete",taskList.get(taskList.size()-1).getOrder()==4);
    }
    
    @Test
    public void testDelete_HasChildren() {
        List<Task> taskList = getMockTaskList();
        Task taskToDelete = taskList.get(1);
        TaskUtil.delete(taskToDelete, taskList);
        assertTrue("Task removed from list",taskList.contains(taskToDelete)==false);
        assertTrue("Task should only have 2 remaining items",taskList.size()==2);
    }

//    @Test
//    public void testMoveDown() {
//    }
//
//    @Test
//    public void testShiftLeft() {
//    }
//
//    @Test
//    public void testShiftRight() {
//    }

}