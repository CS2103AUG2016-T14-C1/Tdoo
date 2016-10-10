package seedu.todoList.model.task;

import seedu.todoList.commons.util.CollectionUtil;

import java.util.Objects;


/**
 * Represents a task in the TodoList.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Task implements ReadOnlyTask {

    protected String name;

    /**
     * Every field must be present and not null.
     */
    public Task(String name) {
        assert !CollectionUtil.isAnyNull(name);
        this.name = name;
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getName());
    }
    
    public Task(){
        
    }
    
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public String toString() {
        return getAsText();
    }


}
