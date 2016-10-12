package seedu.todoList.model;

import javafx.collections.ObservableList;
import seedu.todoList.model.task.ReadOnlyTask;
import seedu.todoList.model.task.Task;
import seedu.todoList.model.task.UniqueTaskList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Wraps all data at the TodoList  level
 * Duplicates are not allowed (by .equals comparison)
 */
public class TaskList implements ReadOnlyTodoList {

    private final UniqueTaskList tasks;

    {
        tasks = new UniqueTaskList();
    }

    public TaskList() {}

    /**
     * tasks and Tags are copied into this TodoList 
     */
<<<<<<< HEAD:src/main/java/seedu/todoList/model/TodoList.java
    public TodoList(ReadOnlyTodoList toBeCopied) {
        this(toBeCopied.getUniqueTaskList());
=======
    public TaskList(ReadOnlyTodoList toBeCopied) {
        this(toBeCopied.getUniqueTaskList(), toBeCopied.getUniqueTagList());
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d:src/main/java/seedu/todoList/model/TaskList.java
    }

    /**
     * tasks and Tags are copied into this TodoList 
     */
<<<<<<< HEAD:src/main/java/seedu/todoList/model/TodoList.java
    public TodoList(UniqueTaskList tasks) {
        resetData(tasks.getInternalList());
=======
    public TaskList(UniqueTaskList tasks, UniqueTagList tags) {
        resetData(tasks.getInternalList(), tags.getInternalList());
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d:src/main/java/seedu/todoList/model/TaskList.java
    }

    public static ReadOnlyTodoList getEmptyTodoList() {
        return new TaskList();
    }

//// list overwrite operations

    public ObservableList<Task> gettasks() {
        return tasks.getInternalList();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks.getInternalList().setAll(tasks);
    }

    public void resetData(Collection<? extends ReadOnlyTask> newtasks) {
        setTasks(newtasks.stream().map(Task::new).collect(Collectors.toList()));
    }

    public void resetData(ReadOnlyTodoList newData) {
        resetData(newData.gettaskList());
    }

//// task-level operations

    /**
     * Adds a task to the TodoList.
     * Also checks the new task's tags and updates {@link #tags} with any new tags found,
     * and updates the Tag objects in the task to point to those in {@link #tags}.
     *
     * @throws UniqueTaskList.DuplicatetaskException if an equivalent task already exists.
     */
    public void addTask(Task p) throws UniqueTaskList.DuplicatetaskException {
        tasks.add(p);
    }

    public boolean removeTask(ReadOnlyTask key) throws UniqueTaskList.taskNotFoundException {
        if (tasks.remove(key)) {
            return true;
        } else {
            throw new UniqueTaskList.taskNotFoundException();
        }
    }

//// util methods

    @Override
    public String toString() {
        return tasks.getInternalList().size() + " tasks";
        // TODO: refine later
    }

    @Override
    public List<ReadOnlyTask> gettaskList() {
        return Collections.unmodifiableList(tasks.getInternalList());
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasks;
    }


    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
<<<<<<< HEAD:src/main/java/seedu/todoList/model/TodoList.java
                || (other instanceof TodoList // instanceof handles nulls
                && this.tasks.equals(((TodoList) other).tasks));
=======
                || (other instanceof TaskList // instanceof handles nulls
                && this.tasks.equals(((TaskList) other).tasks)
                && this.tags.equals(((TaskList) other).tags));
>>>>>>> 599478dd0130f46dae3a83f7da3f6c2fd212f22d:src/main/java/seedu/todoList/model/TaskList.java
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(tasks);
    }
}
