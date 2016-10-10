package seedu.todoList.model.task;


import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.commons.util.CollectionUtil;
import seedu.todoList.logic.commands.CommandResult;
import seedu.todoList.model.task.attributes.Priority;

/**
 * Represents a task's Todo in the TodoList.
 * Guarantees: immutable; is valid as declared in {@link #isValidTodo(String)}
 */
public class Todo extends Task {

    public static final String MESSAGE_Todo_CONSTRAINTS = "task Todoes can be in any format";
    public static final String Todo_VALIDATION_REGEX = ".+";
    
    public static final String MESSAGE_Priority_CONSTRAINTS = "Todo priority index should be a positive integer from index 0";
    public static final String PRIORITY_VALIDATION_REGEX = "[\\p{Digit}]+";
    
    protected String todo;
    protected String priority;
    /**
     * Validates given Todo.
     *
     * @throws IllegalValueException if given Todo string is invalid.
     */

    public Todo(String todo, String priority) throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(todo, priority);
        todo = todo.trim();
        priority = priority.trim();
        if (!isValidTodo(todo)) {
            throw new IllegalValueException(MESSAGE_Todo_CONSTRAINTS);
        }else if (!isValidPriority(priority)) {
            throw new IllegalValueException(MESSAGE_Priority_CONSTRAINTS);
        }
        this.todo = todo;
        this.priority = priority;
        
    }  
    
    public Todo(ReadOnlyTask source) throws IllegalValueException{
        this(source.getTodo(), source.getPriority());
    }
    
    /**
     * Returns true if a given string is a valid task name.
     */
    public static boolean isValidTodo(String todo) {
        return todo.matches(Todo_VALIDATION_REGEX);
    }
    
    /**
     * Returns true if a given string is a valid task priority.
     */
    public static boolean isValidPriority(String priority) {
        return priority.matches(Todo_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return todo;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Todo // instanceof handles nulls
                && this.todo.equals(((Todo) other).todo)); // state check
    }

    @Override
    public int hashCode() {
        return todo.hashCode();
    }
    
    @Override
    public String getTodo() {
        return todo;
    }


    @Override
    public String getPriority() {
        return priority;
    }

    @Override
    public StartTime getStartTime() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public EndTime getEndTime() {
        // TODO Auto-generated method stub
        return null;
    }
}