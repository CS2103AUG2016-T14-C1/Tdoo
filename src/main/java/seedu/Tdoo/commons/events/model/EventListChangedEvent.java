package seedu.Tdoo.commons.events.model;

import seedu.Tdoo.commons.events.BaseEvent;
import seedu.Tdoo.model.ReadOnlyTaskList;

//@@author A0144061U
/** Indicates the TodoList in the model has changed*/
public class EventListChangedEvent extends BaseEvent {

    public final ReadOnlyTaskList data;

    public EventListChangedEvent(ReadOnlyTaskList data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size();
    }
}