package seedu.todoList.logic.commands;

import java.util.HashSet;
import java.util.Set;

import seedu.todoList.commons.exceptions.IllegalValueException;
import seedu.todoList.model.task.*;

/**
 * Adds a task to the TodoList.
 */
public class AddCommand extends Command {

    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the TodoList. "
            + "Parameters: TASK_NAME p/PRIORITY \n"
            + "Example: " + COMMAND_WORD
            + " Assignment 3 p/1";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the Todo list";

    private final Task toAdd;

    /**
     * Convenience constructor using raw values.
     *
     * @throws IllegalValueException if any of the raw values are invalid
     */
    public AddCommand(Name name, int priority)
            throws IllegalValueException {
        this.toAdd = new Todo(name,priority);
    }

    @Override
    public CommandResult execute() {
        assert model != null;
        try {
            model.addTask(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (UniqueTaskList.DuplicatetaskException e) {
            return new CommandResult(MESSAGE_DUPLICATE_TASK);
        }

    }

}
