package seedu.todoList.model.task;

import seedu.todoList.model.task.attributes.Priority;

public class Todo extends Task {

	Priority priority;
	
	public Todo(){
		
	}
	
	public Todo(ReadOnlyTask source){
		priority = source.getPriority();
	}
}
