package seedu.Tdoo.model;

import javafx.collections.ObservableList;
import seedu.Tdoo.model.task.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps all data at the TodoList  level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskList implements ReadOnlyTaskList {

    private UniqueTaskList tasks;
    private Stack<UniqueTaskList> backupStack;

    {
        tasks = new UniqueTaskList();
        backupStack = new Stack<UniqueTaskList>();
    }

    public TaskList() {}

    /**
     * tasks and Tags are copied into this TodoList 
     */
    public TaskList(ReadOnlyTaskList toBeCopied) {
        this(toBeCopied.getUniqueTaskList());
    }

    /**
     * tasks and Tags are copied into this TodoList 
     */
    public TaskList(UniqueTaskList tasks) {
        resetData(tasks.getInternalList());
    }

    public static ReadOnlyTaskList getEmptyTaskList() {
        return new TaskList();
    }

//// list overwrite operations

    public ObservableList<Task> getTasks() {
        return tasks.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
    	this.tasks.getInternalList().setAll(tasks);
    }
    
    //@@author A0144061U
    public void resetData(Collection<? extends ReadOnlyTask> newTasks) {
    	Object[] typeCheck = newTasks.toArray();
    	if(typeCheck.length == 0) {
    		tasks = new UniqueTaskList();
    	}
    	else if(typeCheck[0] instanceof Todo) {
    		setTasks(newTasks.stream().map(Todo::new).collect(Collectors.toList()));
    	}
    	else if(typeCheck[0] instanceof Event) {
    		setTasks(newTasks.stream().map(Event::new).collect(Collectors.toList()));
    	}
    	else if(typeCheck[0] instanceof Deadline) {
    		setTasks(newTasks.stream().map(Deadline::new).collect(Collectors.toList()));
    	}
    }
    
    public void resetData(ReadOnlyTaskList newData) {
        resetData(newData.getTaskList());
    }
    
    public void resetData() {
    	backupStack.push(new UniqueTaskList(tasks));
    	tasks.removeAll();
    }
    
    public void restoreData() {
    	UniqueTaskList backup = backupStack.pop();
    	tasks.setAll(backup);
    }
    //@@author
    

//// task-level operations

    /**
     * Adds a task to the TodoList.
     *
     * @throws UniqueTaskList.DuplicatetaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicatetaskException {
        tasks.add(p);
    }
    
    /**
     * Edit a task to the TodoList.
     *
     * @throws UniqueTaskList.DuplicatetaskException if an equivalent task already exists.
     */
    public void editTask(Task p) throws UniqueTaskList.DuplicatetaskException {
        tasks.edit(p);
        
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    //@@author A0144061U
    public void removeDone() {
    	backupStack.push(new UniqueTaskList(tasks));
    	tasks.removeDone();
    }

    //@@author A0139920A
    public boolean doneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException {
    	if(tasks.doneTask(target)){
    		return true;
    	} else {
    		throw new UniqueTaskList.TaskNotFoundException();
    	}
    }
    
    public boolean undoneTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException {
        if(tasks.undoneTask(target)){
            return true;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
//// util methods

    @Override
    public String toString() {
        return tasks.getInternalList().size() + " tasks";
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return Collections.unmodifiableList(tasks.getInternalList());
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasks;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TaskList // instanceof handles nulls
                && this.tasks.equals(((TaskList) other).tasks));
    }

}