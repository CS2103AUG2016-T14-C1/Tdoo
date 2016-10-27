package seedu.todoList.model.task;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.todoList.commons.exceptions.DuplicateDataException;
import seedu.todoList.commons.util.CollectionUtil;

import java.util.*;

/**
 * A list of tasks that enforces uniqueness between its elements and does not allow nulls.
 *
 * Supports a minimal set of list operations.
 *
 * @see Task#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueTaskList implements Iterable<Task> {

    /**
     * Signals that an operation would have violated the 'no duplicates' property of the list.
     */
    public static class DuplicatetaskException extends DuplicateDataException {
        protected DuplicatetaskException() {
            super("Operation would result in duplicate tasks");
        }
    }

    /**
     * Signals that an operation targeting a specified task in the list would fail because
     * there is no such matching task in the list.
     */
    public static class TaskNotFoundException extends Exception {}

    private final ObservableList<Task> internalList = FXCollections.observableArrayList();

    /**
     * Constructs empty taskList.
     */
    public UniqueTaskList() {}
    
    /**
     * Constructs empty taskList.
     */
    public UniqueTaskList(UniqueTaskList taskList) {
    	setAll(taskList);
    }

    /**
     * Returns true if the list contains an equivalent task as the given argument.
     */
    public boolean contains(ReadOnlyTask toCheck) {
        assert toCheck != null;
        return internalList.contains(toCheck);
    }

    /**
     * Adds a task to the list.
     *
     * @throws DuplicatetaskException if the task to add is a duplicate of an existing task in the list.
     */
    public void add(Task toAdd) throws DuplicatetaskException {
        assert toAdd != null;
        if (contains(toAdd)) {
            throw new DuplicatetaskException();
        }
        internalList.add(toAdd);
    }
    
    /**
     * Edit a task to the list.
     *
     * @throws DuplicatetaskException if the task to update is a duplicate of an existing task in the list.
     */
    public void edit(Task toEdit) throws DuplicatetaskException {
        assert toEdit != null;
        if (contains(toEdit)) {
            throw new DuplicatetaskException();
        }
        ((UniqueTaskList) internalList).edit(toEdit);
    }

    /**
     * Removes the equivalent task from the list.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean remove(ReadOnlyTask toRemove) throws TaskNotFoundException {
        assert toRemove != null;
        final boolean taskFoundAndDeleted = internalList.remove(toRemove);
        if (!taskFoundAndDeleted) {
            throw new TaskNotFoundException();
        }
        return taskFoundAndDeleted;
    }
    
    //@@author A0144061U
    /**
     * Removes all task from the list.
     */
    public void removeAll() {
       internalList.remove(0, internalList.size());
    }
    
    /**
     * Replace all task from another list.
     */
    public void setAll(UniqueTaskList taskList) {
       internalList.setAll(taskList.getInternalList());
    }
    //@@author

    public ObservableList<Task> getInternalList() {
        return internalList;
    }

    @Override
    public Iterator<Task> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueTaskList // instanceof handles nulls
                && this.internalList.equals(
                ((UniqueTaskList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
    
    /**
     * Marks the equivalent task as done.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    //@@author A0139920A
    public boolean doneTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
    	assert key != null;
    	final boolean taskUpdated = internalList.contains(key);
        if (taskUpdated) {
        	internalList.get(internalList.indexOf(key)).setDone("true");
        	internalList.set(internalList.indexOf(key), internalList.get(internalList.indexOf(key)));
            return taskUpdated;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
    
    /**
     * Marks the equivalent task as undone.
     *
     * @throws TaskNotFoundException if no such task could be found in the list.
     */
    public boolean undoneTask(ReadOnlyTask key) throws UniqueTaskList.TaskNotFoundException {
        assert key != null;
        final boolean taskUpdated = internalList.contains(key);
        if (taskUpdated) {
            internalList.get(internalList.indexOf(key)).setDone("false");
            internalList.set(internalList.indexOf(key), internalList.get(internalList.indexOf(key)));
            return taskUpdated;
        } else {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }
}
