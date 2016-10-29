package seedu.todoList.logic.commands;

import seedu.todoList.commons.core.Messages;
import seedu.todoList.commons.core.UnmodifiableObservableList;
import seedu.todoList.commons.events.model.TodoListChangedEvent;
import seedu.todoList.model.task.ReadOnlyTask;
import seedu.todoList.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Selects a task identified using it's last displayed index from the TodoList.
 */
public class DoneCommand extends Command {

    public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD
    		+ ": Mark a Todo-task with given index number as done.\n"
            + "Parameters: TASK_TYPE INDEX_NUMBER(must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " todo 1\n"
            + "Example: " + COMMAND_WORD + " event 1\n"
            + "Example: " + COMMAND_WORD + " deadline 1";

    public static final String MESSAGE_DONE_TASK_SUCCESS = "Completed task: %1$s";
    
    public final String dataType;
    public final int targetIndex;
    
    //@@author A0139920A
    public DoneCommand(String dataType, int targetIndex) {
    	this.dataType = dataType;
        this.targetIndex = targetIndex;
    }

    //@@author A0139920A
    @Override
    public CommandResult execute() {
    	
    	UnmodifiableObservableList<ReadOnlyTask> lastShownList = null;
    	switch (dataType) {
    		case "todo":
    			lastShownList = model.getFilteredTodoList();
    			break;
    		case "event":
    			lastShownList = model.getFilteredEventList();
    			break;
    		case "deadline":
    			lastShownList = model.getFilteredDeadlineList();
    	}
        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        ReadOnlyTask taskToDone = lastShownList.get(targetIndex - 1);

        try {
            model.doneTask(taskToDone, dataType, targetIndex);
        } catch (TaskNotFoundException pnfe) {
            assert false : "The target task cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DONE_TASK_SUCCESS, taskToDone));
    }

}
