package seedu.todoList.model.task;

import seedu.todoList.commons.util.CollectionUtil;

import java.util.Objects;


/**
 * Represents a task in the TodoList.
 * Guarantees: details are present and not null, field values are validated.
 */
public abstract class Task implements ReadOnlyTask {

    private Todo todo;
    private StartTime startTime;
    private EndTime endTime;

    /**
     * Every field must be present and not null.
     */
    public Task(Todo todo, StartTime startTime, EndTime endTime) {
        assert !CollectionUtil.isAnyNull(todo, startTime, endTime);
        this.todo = todo;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getTodo(), source.getStartTime(), source.getEndTime());
    }
    
    @Override
    public Todo getTodo() {
        return todo;
    }


    @Override
    public StartTime getStartTime() {
        return startTime;
    }

    @Override
    public EndTime getEndTime() {
        return endTime;
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
