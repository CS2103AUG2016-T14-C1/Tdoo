package seedu.todoList.model.task;


import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.commons.util.CollectionUtil;
import seedu.todoList.logic.commands.CommandResult;
import seedu.todoList.model.task.attributes.Date;
import seedu.todoList.model.task.attributes.Priority;

/**
 * Represents a task's Todo in the TodoList.
 * Guarantees: immutable; is valid as declared in {@link #isValidTodo(String)}
 */
public class Todo extends Task {
    
    public static final String MESSAGE_Priority_CONSTRAINTS = "Todo priority index should be a positive integer from index 0";
    public static final String Priority_VALIDATION_REGEX = "[\\p{Digit}]+";
    
    private int priority;
    private Name name;
    /**
     * Validates given Todo.
     *
     * @throws IllegalValueException if given Todo string is invalid.
     */

    public Todo(Name name, int priority) throws IllegalValueException {
        assert !CollectionUtil.isAnyNull(priority);
        if (!isValidPriority(Integer.toString(priority))) {
            throw new IllegalValueException(MESSAGE_Priority_CONSTRAINTS);
        }
        this.name = name;
        this.priority = priority;
       
    }  
    
    public Todo(ReadOnlyTask source) throws IllegalValueException{
        this(source.getName(), source.getPriority());
    }
    
    /**
     * Returns true if a given string is a valid task priority.
     */
    public static boolean isValidPriority(String priority) {
        return priority.matches(Priority_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Todo // instanceof handles nulls
                && this.name.equals(((Todo) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public int getPriority() {
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