package seedu.todoList.model.task;

import seedu.todoList.model.task.attributes.Date;
import seedu.todoList.model.task.attributes.EndTime;

public class Deadline {

	Date date;
	EndTime endTime;
	
	public Deadline(){
		
	}
	
	public Deadline(ReadOnlyTask source){
		date = source.getDate();
		endTime = source.getEndTime();
	}
}
