package seedu.todoList.model.task;

import seedu.todoList.model.task.attributes.StartTime;
import seedu.todoList.model.task.attributes.EndTime;
import seedu.todoList.model.task.attributes.Priority;
import seedu.todoList.model.task.attributes.Date;
import seedu.todoList.model.task.attributes.Name;

/**
 * A read-only immutable interface for a task in the TodoList .
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Todo getTodo();
    Name getName();
    StartTime getStartTime();
    EndTime getEndTime();
    Priority getPriority();
    Date getDate();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTodo().equals(this.getTodo()));// state checks here onwards
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(" Todo: ")
                .append(getTodo());
        return builder.toString();
    }

}
