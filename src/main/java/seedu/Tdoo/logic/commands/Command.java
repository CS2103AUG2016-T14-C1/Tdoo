package seedu.Tdoo.logic.commands;

import seedu.Tdoo.commons.core.EventsCenter;
import seedu.Tdoo.commons.core.Messages;
import seedu.Tdoo.commons.events.ui.IncorrectCommandAttemptedEvent;
import seedu.Tdoo.model.Model;
import seedu.Tdoo.storage.Storage;
/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {
    protected Model model;
    protected Storage storage;

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of tasks.
     *
     * @param displaySize used to generate summary
     * @return summary message for tasks displayed
     */
    public static String getMessageFortaskListShownSummary(int displaySize) {
        if(displaySize == 0){
            return String.format(Messages.MESSAGE_tasks_NOTFOUND, displaySize);
        }else{
            return String.format(Messages.MESSAGE_taskS_LISTED_OVERVIEW, displaySize);
        }
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model, Storage storage) {
        this.model = model;
        this.storage = storage;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
}
