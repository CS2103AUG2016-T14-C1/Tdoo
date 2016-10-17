package seedu.todoList.logic.commands;

import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.model.task.*;
import seedu.todoList.model.task.attributes.*;

/**
 * Edit a task identified using it's last displayed index from the TodoList.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Edits information of the task in the task-list.\n"
            + "Parameters: TASK_TYPE INDEX_NUMBER n/TASK_NAME p/PRIORITY\n"
            + "Example: " + COMMAND_WORD + " edit todo 1 n/Assignment 1 p/2\n"
            + "Parameters: TASK_TYPE INDEX_NUMBER n/TASK_NAME d/DATE s/START_TIME e/END_TIME"
            + "Example: " + COMMAND_WORD + " edit event 1 n/Time's birthday party d/25-12-2016 s/1200 e/1600\n"
            + "Parameters: TASK_TYPE INDEX_NUMBER n/TASK_NAME d/DATE e/END_TIME"
            + "Example: " + COMMAND_WORD + " edit deadline 1 n/CS2103 v0.2 d/25-12-2016 e/1400";

    public static final String MESSAGE_EDIT_task_SUCCESS = "Edited task: %1$s";
    public static final String INVALID_VALUE = "Invalid value";
    
    private final Task toEdit;
    
    /**
     * Edit Todo
     * Convenience constructor using raw values.
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(String name, String priority)
            throws IllegalValueException {
        this.toEdit = new Todo(
                new Name(name),
                new Priority(priority)
        );
    }
    
    /**
     * Edit Event
     * Convenience constructor using raw values.
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(String name, String date, String startTime, String endTime)
            throws IllegalValueException {
        this.toEdit = new Event(
                new Name(name),
                new Date(date),
                new StartTime(startTime),
                new EndTime(endTime)
        );
    }
    
    /**
     * Edit Deadline
     * Convenience constructor using raw values.
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public EditCommand(String name, String date, String endTime)
            throws IllegalValueException {
        this.toEdit = new Deadline(
                new Name(name),
                new Date(date),
                new EndTime(endTime)
        );
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.editTask(toEdit);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toEdit));
        } catch (UniqueTaskList.DuplicatetaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        } catch (IllegalValueException ive) {
        	return new CommandResult(INVALID_VALUE);
        }
    }
}
