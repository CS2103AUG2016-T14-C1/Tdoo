package seedu.todoList.model.task;

import seedu.todoList.model.task.attributes.Priority;

/**
 * A read-only immutable interface for a task in the TodoList .
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Name getName();
    int getPriority();
    StartTime getStartTime();
    EndTime getEndTime();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName())// state checks here onwards
                && Integer.toString(other.getPriority()).equals(this.getPriority()));
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Todo: ")
                .append(getName())
                .append("\nPriority")
                .append(getPriority());
        return builder.toString();
    }

}
