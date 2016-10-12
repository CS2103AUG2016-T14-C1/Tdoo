package seedu.todoList.model.task;

import seedu.todoList.model.task.attributes.Date;
import seedu.todoList.model.task.attributes.StartTime;
import seedu.todoList.model.task.attributes.EndTime;

public class Event {

	Date date;
	StartTime startTime;
	EndTime endTime;
	
	public Event(){
		
	}
	
	public Event(ReadOnlyTask source){
		date = source.getDate();
		startTime = source.getStartTime();
		endTime = source.getEndTime();
	}
}
